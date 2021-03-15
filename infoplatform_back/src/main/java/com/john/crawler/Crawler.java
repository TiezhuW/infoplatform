package com.john.crawler;

import com.john.repository.HouseRepository;
import com.john.entity.House;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Slf4j
public abstract class Crawler {
    private int timeWait;
    private Map<String, String> cityAbbrMap = new HashMap<>();
    private HouseRepository houseRepository;


    public Crawler() {
    }

    public Crawler(int timeWait) {
        this.timeWait = timeWait;
    }

    public abstract Map<String, String> getDistrictUrlMap(String city) throws IOException, InterruptedException;

    public abstract Set<House> getHouseUrls(Map<String, String> districtUrlMap, String city, String district, int pages) throws IOException, InterruptedException;

    public abstract void getHouseInfo(House house) throws IOException, InterruptedException;

    public void crawl(String city, int pages) throws IOException, InterruptedException{
        Map<String, String> districtUrlMap = getDistrictUrlMap(city);

        for (Map.Entry<String, String> districtUrlEntry: districtUrlMap.entrySet()) {
            Set<House> houses = getHouseUrls(districtUrlMap, city, districtUrlEntry.getKey(), pages);

            for (House house: houses) {
                getHouseInfo(house);
//                System.out.println(house);
                log.info(house.toString());
                houseRepository.save(house);
            }
        }
    }


    public int getTimeWait() {
        return timeWait;
    }

    public void setTimeWait(int timeWait) {
        this.timeWait = timeWait;
    }

    public Map<String, String> getCityAbbrMap() {
        return cityAbbrMap;
    }

    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }
}
