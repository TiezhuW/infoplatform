package com.john.crawler;

import com.john.repository.HouseRepository;
import com.john.entity.House;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class CrawlerBk extends Crawler{
    {
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        cityAbbrMap.put("上海", "sh");
        cityAbbrMap.put("杭州", "hz");
        cityAbbrMap.put("南京", "nj");
        cityAbbrMap.put("苏州", "su");
    }


    public CrawlerBk() {
        super();
    }

    public CrawlerBk(int timeWait) {
        super(timeWait);
    }


    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        super.setHouseRepository(houseRepository);
    }


    @Override
    public Map<String, String> getDistrictUrlMap(String city) throws IOException, InterruptedException {
        Map<String, String> districtUrlMap = new HashMap<>();
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        String cityAbbr = cityAbbrMap.get(city);
        String cityUrl = "https://" + cityAbbr + ".ke.com/ershoufang/";

        Document cityDoc = Jsoup.connect(cityUrl).get();
        int timeWait = getTimeWait() * 1000;
        Thread.sleep(timeWait);

        Elements cityElements = cityDoc.select("a[data-click-event=WebModuleClick]");
        for (Element cityElement: cityElements) {
            districtUrlMap.put(cityElement.text(), cityUrl + cityElement.attr("href").substring(12));
        }

        return districtUrlMap;
    }

    @Override
    public Set<House> getHouseUrls(Map<String, String> districtUrlMap, String city, String district, int pages) throws IOException, InterruptedException {
        Set<House> houses = new HashSet<>();
        String districtUrl = districtUrlMap.get(district);

        for (int page = 1; page <= pages; page++) {
            Document districtDoc = Jsoup.connect(districtUrl + "pg" + page).get();
            int timeWait = getTimeWait() * 1000;
            Thread.sleep(timeWait);
            Elements districtElements = districtDoc.select("a[data-click-event=SearchClick]");

            for (Element districtElement: districtElements) {
                String houseUrl = districtElement.attr("href");
                House house = new House();
                house.setCity(city);
                house.setDistrict(district);
                house.setUrl(houseUrl);
                houses.add(house);
            }
        }

        return houses;
    }

    @Override
    public void getHouseInfo(House house) throws IOException, InterruptedException {
        String houseUrl = house.getUrl();
        Document houseDoc = Jsoup.connect(houseUrl).get();
        int timeWait = getTimeWait() * 1000;
        Thread.sleep(timeWait);

        Integer price = Integer.valueOf(houseDoc.select("span.unitPriceValue").first().text());
        String region = houseDoc.select("div.areaName span.info a").get(1).text();
        String community = houseDoc.select("a.info").first().text();
        String address = region + "-" + community;

        Float area = null;
        String floor = null;
        String lift = null;
        String houseType = null;
        String orientation = null;

        Elements houseElements = houseDoc.select("div.content li");
        for (Element houseElement: houseElements) {
            String houseInfoLabel = houseElement.child(0).text();
            String houseInfoContent = houseElement.ownText();

            switch(houseInfoLabel) {
                case "建筑面积": {
                    area = Float.valueOf(houseInfoContent.substring(0, houseInfoContent.indexOf("㎡")));
                    break;
                }
                case "所在楼层": {
                    floor = houseInfoContent;
                    break;
                }
                case "梯户比例": {
                    lift = houseInfoContent;
                    break;
                }
                case "房屋户型": {
                    houseType = houseInfoContent;
                    break;
                }
                case "房屋朝向": {
                    orientation = houseInfoContent;
                    break;
                }
            }
        }

        house.setArea(area);
        house.setPrice(price);
        house.setAddress(address);
        house.setFloor(floor);
        house.setLift(lift);
        house.setHouseType(houseType);
        house.setOrientation(orientation);
    }


//    public static void main(String[] args) {
//        try {
//            Crawler crawler = new CrawlerBk(1);
//            crawler.crawl("杭州", 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
