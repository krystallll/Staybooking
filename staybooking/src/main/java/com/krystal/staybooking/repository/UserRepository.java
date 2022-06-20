package com.krystal.staybooking.repository;

import com.krystal.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//自己实现增删改查
//保存用户名 密码 权限
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}