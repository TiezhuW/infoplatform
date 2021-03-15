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
public class CrawlerAjk extends Crawler{
    {
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        cityAbbrMap.put("上海", "shanghai");
        cityAbbrMap.put("杭州", "hangzhou");
        cityAbbrMap.put("南京", "nanjing");
        cityAbbrMap.put("苏州", "suzhou");
    }


    public CrawlerAjk() {
        super();
    }

    public CrawlerAjk(int timeWait) {
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
        String cityUrl = "https://" + cityAbbr + ".anjuke.com/sale/";

        Document cityDoc = Jsoup.connect(cityUrl).get();
        int timeWait = getTimeWait() * 1000;
        Thread.sleep(timeWait);

        Elements cityElements = cityDoc.select("a[data-npv=esf_List_quyu]");
        for (Element cityElement: cityElements) {
            if (!cityElement.text().equals("杭州周边") && !cityElement.text().equals("不限")) {
                districtUrlMap.put(cityElement.text(), cityElement.attr("href"));
            }
        }

        return districtUrlMap;
    }

    @Override
    public Set<House> getHouseUrls(Map<String, String> districtUrlMap, String city, String district, int pages) throws IOException, InterruptedException {
        Set<House> houses = new HashSet<>();
        String districtUrl = districtUrlMap.get(district);

        for (int page = 1; page <= pages; page++) {
            Document districtDoc = Jsoup.connect(districtUrl + "p" + page).get();
            int timeWait = getTimeWait() * 1000;
            Thread.sleep(timeWait);
            Elements districtElements = districtDoc.select("a.property-ex");

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

        Float area = null;
        Integer price = null;
        String region = null;
        String community = null;
        String floor = null;
        String houseType = null;
        String orientation = null;
        String buildYear = null;

        String priceStr = houseDoc.select("div.maininfo-avgprice-price").first().text();
        price = Integer.valueOf(priceStr.substring(0, priceStr.indexOf(".")));

        houseType = houseDoc.select("div.maininfo-model-strong").first().text();

        String areaStr = houseDoc.select("div.maininfo-model-item-2 i").first().text();
        area = Float.valueOf(areaStr);

        orientation = houseDoc.select("i.maininfo-model-strong-text").first().text();

        floor = houseDoc.select("div.maininfo-model-weak").first().text();

        buildYear = houseDoc.select("div.maininfo-model-weak").last().text().substring(0, 5);

        region = houseDoc.select("span.maininfo-community-item-name").first().text();
        community = houseDoc.select("div.maininfo-community-item").first().getElementsByTag("a").first().text();
        String address = region + "-" + community;

        house.setArea(area);
        house.setPrice(price);
        house.setAddress(address);
        house.setFloor(floor);
        house.setHouseType(houseType);
        house.setOrientation(orientation);
        house.setBuildYear(buildYear);
    }


//    public static void main(String[] args) {
//        try{
//            Crawler crawler = new CrawlerAjk(5);
//            crawler.crawl("杭州", 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
