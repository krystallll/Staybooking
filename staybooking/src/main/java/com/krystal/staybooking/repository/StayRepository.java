package com.krystal.staybooking.repository;

import com.krystal.staybooking.model.Stay;
import com.krystal.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//对stay class进行增删改查
@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

    List<Stay> findByHost(User user); //select * from stay table where host = user

    Stay findByIdAndHost(Long id, User host); //不能随便删id id和host匹配上 比如： 1号 a用户 2号 b用户

    //在集合里搜索 不是整个表 筛选条件是大于等于guest number
    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);

}
