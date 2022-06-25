package com.krystal.staybooking.filter;

import com.krystal.staybooking.model.Authority;
import com.krystal.staybooking.repository.AuthorityRepository;
import com.krystal.staybooking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;



@Component
//filter只执行一次
public class JwtFilter extends OncePerRequestFilter {
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private AuthorityRepository authorityRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public JwtFilter (AuthorityRepository authorityRepository, JwtUtil jwtUtil) {
        this.authorityRepository = authorityRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader(HEADER);

        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith(PREFIX)) {
            jwt = authorizationHeader.substring(PREFIX.length()); //authorization
        }

        //guarantee只会验证token一次 系统不需要再验证 直接去访问  validate => 有没有超时
        //SecurityContextHolder=>check之前有没有验证过 已经带上token 不需要再做验证了
        if (jwt != null && jwtUtil.validateToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtil.extractUsername(jwt); //找到请求的用户是谁
            Authority authority = authorityRepository.findById(username).orElse(null);
            if (authority != null) {//找到authority
                //token是collection  类型要继承grantedAuthorities list是collection的子类 根据type创建出来的集合
                List<GrantedAuthority> grantedAuthorities = Arrays.asList(new GrantedAuthority[]{
                        new SimpleGrantedAuthority(authority.getAuthority())
                });
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, grantedAuthorities); //生成token
                //把token保存起来
                //保存用户information的token
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);//请求交给下一个filter去执行
    }

}
