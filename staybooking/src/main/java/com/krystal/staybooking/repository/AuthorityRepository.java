package com.krystal.staybooking.repository;

import com.krystal.staybooking.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//string根据primary key的类型决定的
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findAuthorityByUsername(String username);

}
