package com.krystal.staybooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import javax.sql.DataSource;
import org.springframework.security.authentication.AuthenticationManager;


//加密信息
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //springboot自己创建 类似database的driver
    @Autowired
    private DataSource dataSource;

    @Bean
    //自动encryption
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //判断有没有这个url的权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register/*").permitAll()
                .antMatchers(HttpMethod.POST, "/authenticate/*").permitAll()
                .antMatchers("/stays").permitAll()
                .antMatchers("/stays/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable();
    }

    //两个query：自己找到数据去匹配
    //authenticate http request -> 检查用户能不能登陆 username和password
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, authority FROM authority WHERE username = ?");
    }

    //作为bean 让spring创建出来后 service就可以call这个方法去验证用户密码
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}





