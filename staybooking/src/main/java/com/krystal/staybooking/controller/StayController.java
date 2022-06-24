package com.krystal.staybooking.controller;

import com.krystal.staybooking.model.Stay;
import com.krystal.staybooking.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.krystal.staybooking.model.User;

import java.util.List;

@RestController
public class StayController {
    private StayService stayService;

    @Autowired
    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @GetMapping(value = "/stays")
    public List<Stay> listStays(@RequestParam(name = "host") String hostName) {
        return stayService.listByUser(hostName);
    }

    @GetMapping(value = "/stays/id")
    public Stay getStay(
            @RequestParam(name = "stay_id") Long stayId,
            @RequestParam(name = "host") String hostName) {
        return stayService.findByIdAndHost(stayId, hostName);
    }

    //add
    @PostMapping("/stays")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("host") String host,
            @RequestParam("guest_number") int guestNumber,
            @RequestParam("images") MultipartFile[] images) {

        Stay stay = new Stay.Builder().setName(name)
                .setAddress(address)
                .setDescription(description)
                .setGuestNumber(guestNumber)
                .setHost(new User.Builder().setUsername(host).build())
                .build();
        stayService.add(stay, images);
    }

        //delete
    @DeleteMapping("/stays")
    public void deleteStay(
            @RequestParam(name = "stay_id") Long stayId,
            @RequestParam(name = "host") String hostName) {
        stayService.delete(stayId, hostName);
    }


}
