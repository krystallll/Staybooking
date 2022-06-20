package com.krystal.staybooking.service;

import com.krystal.staybooking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.krystal.staybooking.exception.UserNotExistException;
import com.krystal.staybooking.model.Token;
import com.krystal.staybooking.model.User;
import com.krystal.staybooking.model.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class AuthenticationService {

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    //1.判断用户密码
    //2.权限
    public Token authenticate(User user, UserRole role) throws UserNotExistException {
        Authentication auth = null;
        try {
            //controller获得的用户名和密码
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException exception) {
            throw new UserNotExistException("User Doesn't Exist");
        }

        //把用户的权限从数据库里拿出来存到了authentication这个对象里
        if (auth == null || !auth.isAuthenticated() || !auth.getAuthorities().contains(new SimpleGrantedAuthority(role.name()))) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        return new Token(jwtUtil.generateToken(user.getUsername()));
    }

}















