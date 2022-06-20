package com.krystal.staybooking.service;

import com.krystal.staybooking.exception.UserAlreadyExistException;
import com.krystal.staybooking.model.Authority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.krystal.staybooking.repository.AuthorityRepository;
import com.krystal.staybooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.krystal.staybooking.model.User;
import com.krystal.staybooking.model.UserRole;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterService {

    private UserRepository userRepository;

    private AuthorityRepository authorityRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)//独立的 跟其他部分无关
    //保证操作是atomic 都是成功的 某一个出错了 自动rollback 不用自己手动写
    public void add(User user, UserRole role) {
    //被注册过抛异常
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }

    //加密信息



}