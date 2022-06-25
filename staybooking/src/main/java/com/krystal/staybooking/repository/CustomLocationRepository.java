package com.krystal.staybooking.repository;

import java.util.List;

//单独写的话 比较放心
public interface CustomLocationRepository {
    List<Long> searchByDistance(double lat, double lon, String distance);
}
