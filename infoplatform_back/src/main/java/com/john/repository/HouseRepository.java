package com.john.repository;

import com.john.entity.House;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface HouseRepository {
    List<House> findByCityDistrictDefault(String city, String district, Float minArea, Float maxArea, Integer minPrice, Integer maxPrice);
    List<House> findByCityDistrictAreaAsc(String city, String district, Float minArea, Float maxArea, Integer minPrice, Integer maxPrice);
    List<House> findByCityDistrictAreaDesc(String city, String district, Float minArea, Float maxArea, Integer minPrice, Integer maxPrice);
    List<House> findByCityDistrictPriceAsc(String city, String district, Float minArea, Float maxArea, Integer minPrice, Integer maxPrice);
    List<House> findByCityDistrictPriceDesc(String city, String district, Float minArea, Float maxArea, Integer minPrice, Integer maxPrice);
    House findById(Integer id);
    int save(House house);
    int update(House house);
    int deleteById(Integer id);
}
