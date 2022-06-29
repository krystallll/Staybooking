package com.krystal.staybooking.service;
import com.krystal.staybooking.exception.StayDeleteException;
import com.krystal.staybooking.exception.StayNotExistException;
import com.krystal.staybooking.repository.LocationRepository;
import com.krystal.staybooking.repository.ReservationRepository;
import com.krystal.staybooking.repository.StayReservationDateRepository;
import org.springframework.stereotype.Service;
import com.krystal.staybooking.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.krystal.staybooking.model.*;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayRepository stayRepository;

    private ImageStorageService imageStorageService;

    private LocationRepository locationRepository;

    private GeoCodingService geoCodingService;

    private ReservationRepository reservationRepository;

    private StayReservationDateRepository stayReservationDateRepository; //删除一个房子的时候 同时要把预定过的信息给删掉
    // table reserved date table是个组合键

    @Autowired
    public StayService(StayRepository stayRepository, ImageStorageService imageStorageService, LocationRepository locationRepository, GeoCodingService geoCodingService, ReservationRepository reservationRepository, StayReservationDateRepository stayReservationDateRepository) {
        this.stayRepository = stayRepository;
        this.imageStorageService = imageStorageService;
        this.locationRepository = locationRepository;
        this.geoCodingService = geoCodingService;
        this.reservationRepository = reservationRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;

    }

    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public Stay findByIdAndHost(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        return stay;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Stay stay, MultipartFile[] images) {
        //获得image url
        List<String> mediaLinks = Arrays.stream(images)
                .parallel().map(image -> imageStorageService.save(image))
                .collect(Collectors.toList());
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
        stayRepository.save(stay);

        //既存在了mysql下 也存到了elastic search底下
        Location location = geoCodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long stayId, String username) throws StayNotExistException, StayDeleteException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        //现在之后有没有guest预定这个房子
        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(stay, LocalDate.now());
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
        //可以删掉的话 所有这些数据删掉

        List<StayReservedDate> stayReservedDates = stayReservationDateRepository.findByStay(stay);

        for(StayReservedDate date : stayReservedDates) {
            stayReservationDateRepository.deleteById(date.getId());
        }

        stayRepository.deleteById(stayId);
    }


}