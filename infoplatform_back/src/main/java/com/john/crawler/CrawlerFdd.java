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
public class CrawlerFdd extends Crawler{
    {
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        cityAbbrMap.put("上海", "shanghai");
        cityAbbrMap.put("杭州", "hangzhou");
        cityAbbrMap.put("南京", "nanjing");
        cityAbbrMap.put("苏州", "suzhou");
    }


    public CrawlerFdd() {
        super();
    }

    public CrawlerFdd(int timeWait) {
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
        String cityUrl = "https://" + cityAbbr + ".fangdd.com/esf/";

        Document cityDoc = Jsoup.connect(cityUrl).get();
        int timeWait = getTimeWait() * 1000;
        Thread.sleep(timeWait);

        Elements cityElements = cityDoc.select("a[data-track-event=scsem3]");
        String baseUrl = "https://" + cityAbbr + ".fangdd.com";
        for (Element cityElement: cityElements) {
            if (!cityElement.text().equals("不限") && !cityElement.text().equals("杭州周边")) {
                districtUrlMap.put(cityElement.text(), baseUrl + cityElement.attr("href"));
            }
        }

        return districtUrlMap;
    }

    @Override
    public Set<House> getHouseUrls(Map<String, String> districtUrlMap, String city, String district, int pages) throws IOException, InterruptedException {
        Set<House> houses = new HashSet<>();
        String districtUrl = districtUrlMap.get(district);

        for (int page = 1; page <= pages; page++) {
            Document districtDoc = Jsoup.connect(districtUrl + "?pageNo=" + page).get();
            int timeWait = getTimeWait() * 1000;
            Thread.sleep(timeWait);

            Elements districtElements = districtDoc.select("h4.LpList-name > a");
            Map<String, String> cityAbbrMap = getCityAbbrMap();
            String cityAbbr = cityAbbrMap.get(city);
            String baseUrl = "https://" + cityAbbr + ".fangdd.com";
            for (Element districtElement: districtElements) {
                String houseUrl = baseUrl + districtElement.attr("href");
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

        String priceStr = houseDoc.select("p.BasicDetail-esf-base-single").first().text();
        Integer price = Integer.valueOf(priceStr.substring(0, priceStr.indexOf("元")));
        StringBuilder sb = new StringBuilder();
        sb.append(houseDoc.select("li.InfoList-item--w > span.InfoList-text > a").get(2).text());
        sb.append("-");
        sb.append(houseDoc.select("li.InfoList-item--w > span.InfoList-text > a").get(3).text());
        sb.append("-");
        sb.append(houseDoc.select("li.InfoList-item--w > span.InfoList-text > a").get(0).text());
        String address =  sb.toString();
        String lift = null;
        Elements lpListLabelItems = houseDoc.select("div.LpList-label").first().children();
        for (Element lpListLabelItem: lpListLabelItems) {
            if (lpListLabelItem.text().equals("有电梯") || lpListLabelItem.text().equals("电梯房")) {
                lift = "有";
                break;
            }
        }
        String buildYear = houseDoc.select("p.BasicDetail-esf-info-label").get(2).text();

        Elements houseElements = houseDoc.select("strong.BasicDetail-esf-info-text");
        String areaStr = houseElements.get(1).text();
        Float area = Float.valueOf(areaStr.substring(0, areaStr.indexOf("㎡")));
        String houseType = houseElements.get(0).text();
        String orientation = houseElements.get(2).text();

        house.setArea(area);
        house.setPrice(price);
        house.setAddress(address);
        house.setLift(lift);
        house.setHouseType(houseType);
        house.setOrientation(orientation);
        house.setBuildYear(buildYear);
    }


//    public static void main(String[] args) {
//        Crawler crawler = new CrawlerFdd(1);
//        try {
//            crawler.crawl("杭州", 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
