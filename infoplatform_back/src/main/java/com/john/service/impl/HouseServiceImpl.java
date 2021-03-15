package com.john.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.john.crawler.*;
import com.john.repository.HouseRepository;
import com.john.entity.House;
import com.john.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private Crawler crawlerAjk;

    @Autowired
    private Crawler crawlerBk;

    @Autowired
    private Crawler crawlerFdd;

    @Autowired
    private CrawlerFtx crawlerFtx;

    @Autowired
    private CrawlerWawj crawlerWawj;

    @Autowired
    private CrawlerZgzf crawlerZgzf;


    @Override
    public PageInfo findByCityDistrict(String city, String district, int pageNum, int pageSize,
                                       Float minArea, Float maxArea, Integer minPrice, Integer maxPrice, String order) {
        List<House> houses = null;
        switch (order) {
            case "默认排序": {
                PageHelper.startPage(pageNum, pageSize);
                houses = houseRepository.findByCityDistrictDefault(city, district, minArea, maxArea, minPrice, maxPrice);
                break;
            }
            case "面积升序": {
                PageHelper.startPage(pageNum, pageSize);
                houses = houseRepository.findByCityDistrictAreaAsc(city, district, minArea, maxArea, minPrice, maxPrice);
                break;
            }
            case "面积降序": {
                PageHelper.startPage(pageNum, pageSize);
                houses = houseRepository.findByCityDistrictAreaDesc(city, district, minArea, maxArea, minPrice, maxPrice);
                break;
            }
            case "价格升序": {
                PageHelper.startPage(pageNum, pageSize);
                houses = houseRepository.findByCityDistrictPriceAsc(city, district, minArea, maxArea, minPrice, maxPrice);
                break;
            }
            case "价格降序": {
                PageHelper.startPage(pageNum, pageSize);
                houses = houseRepository.findByCityDistrictPriceDesc(city, district, minArea, maxArea, minPrice, maxPrice);
                break;
            }
        }
        PageInfo pageInfo = new PageInfo(houses);
        return pageInfo;
    }

    @Override
    public House findById(Integer id) {
        return houseRepository.findById(id);
    }

    @Override
    public int save(House house) {
        return houseRepository.save(house);
    }

    @Override
    public int update(House house) {
        return houseRepository.update(house);
    }

    @Override
    public int deleteById(Integer id) {
        return houseRepository.deleteById(id);
    }

    @Override
    public void crawlAjk(String city, int pages) {
        try {
            crawlerAjk.setTimeWait(3);
            crawlerAjk.crawl(city, pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void crawlBk(String city, int pages) {
        try {
            crawlerBk.setTimeWait(1);
            crawlerBk.crawl(city, pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void crawlFdd(String city, int pages) {
        try {
            crawlerFdd.setTimeWait(1);
            crawlerFdd.crawl(city, pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void crawlFtx(String city, int pages) {
        try {
            crawlerFtx.setTimeWait(14);
            crawlerFtx.crawl(city, pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void crawlWawj(String city, int pages) {
        try {
            crawlerWawj.setTimeWait(10);
            crawlerWawj.crawl(city, pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void crawlZgzf(String city, int pages) {
        try {
            crawlerZgzf.setTimeWait(10);
            crawlerZgzf.crawl(city, pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
