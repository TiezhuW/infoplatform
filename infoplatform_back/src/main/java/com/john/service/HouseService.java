package com.john.service;

import com.github.pagehelper.PageInfo;
import com.john.entity.House;

public interface HouseService {
    PageInfo findByCityDistrict(String city, String district, int pageNum, int pageSize,
                                Float minArea, Float maxArea, Integer minPrice, Integer maxPrice, String order);
    House findById(Integer id);
    int save(House house);
    int update(House house);
    int deleteById(Integer id);
    void crawlAjk(String city, int pages);
    void crawlBk(String city, int pages);
    void crawlFdd(String city, int pages);
    void crawlFtx(String city, int pages);
    void crawlWawj(String city, int pages);
    void crawlZgzf(String city, int pages);
}
