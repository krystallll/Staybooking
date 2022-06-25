package com.krystal.staybooking.repository;
import com.krystal.staybooking.model.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// LocationRepository also provides some basic query functions like find(), save() and delete().
// But since our service needs to support search based on Geolocation, we need to implement the search function ourselves.
@Repository
public interface LocationRepository extends ElasticsearchRepository<Location, Long>, CustomLocationRepository {

}

