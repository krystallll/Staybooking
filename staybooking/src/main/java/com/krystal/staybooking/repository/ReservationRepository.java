package com.krystal.staybooking.repository;

import com.krystal.staybooking.model.Reservation;
import com.krystal.staybooking.model.Stay;
import com.krystal.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuest(User guest); //查找

    List<Reservation> findByStay(Stay stay); //查找

    Reservation findByIdAndGuest(Long id, User guest); // for deletion

    //save 和 add用默认的

    //删除的时间点 看后面有没有reservation的data
    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);

}
