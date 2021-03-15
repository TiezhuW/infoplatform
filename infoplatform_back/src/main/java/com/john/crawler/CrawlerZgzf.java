package com.john.crawler;

import com.john.repository.HouseRepository;
import com.john.entity.House;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CrawlerZgzf extends Crawler {
    private WebDriver driver;


    {
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        cityAbbrMap.put("上海", "sh");
        cityAbbrMap.put("杭州", "hz");
        cityAbbrMap.put("南京", "nj");
        cityAbbrMap.put("苏州", "su");

        System.setProperty("webdriver.edge.driver", "D:\\WebDriver\\msedgedriver.exe");
        driver = new EdgeDriver();

//        System.setProperty("webdriver.chrome.driver", "D:\\WebDriver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);
//        driver = new ChromeDriver(options);
    }


    public CrawlerZgzf() {
        super();
    }

    public CrawlerZgzf(int timeWait) {
        super(timeWait);
        driver.manage().timeouts().implicitlyWait(timeWait, TimeUnit.SECONDS);
    }


    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        super.setHouseRepository(houseRepository);
    }


    @Override
    public Map<String, String> getDistrictUrlMap(String city){
        Map<String, String> districtUrlMap = new HashMap<>();
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        String cityAbbr = cityAbbrMap.get(city);
        String cityUrl = "https://" + cityAbbr + ".ershoufang.zhuge.com/";

        driver.get(cityUrl);

        List<WebElement> cityElements = driver.findElements(By.className("l-filtrate-box")).get(1).findElements(By.className("list-screen-link"));
        for (WebElement cityElement: cityElements) {
            if (!(cityElement.getText().equals("全部") || cityElement.getText().equals("杭州周边"))){
                districtUrlMap.put(cityElement.getText(), cityElement.getAttribute("href"));
            }
        }

        return districtUrlMap;
    }

    @Override
    public Set<House> getHouseUrls(Map<String, String> districtUrlMap, String city, String district, int pages) {
        Set<House> houses = new HashSet<>();
        String districtUrl = districtUrlMap.get(district);
        Map<String, String> cityAbbrMap = getCityAbbrMap();

        for (int page = 1; page <= pages; page++) {
            driver.get(districtUrl + "page/" + page);

            List<WebElement> districtElements = driver.findElements(By.xpath("//span[@class='list-table-info-box']/div[@class='title']/a"));
            for (WebElement districtElement: districtElements) {
                String houseUrl = districtElement.getAttribute("href");

                if (houseUrl.substring(8, 10).equals(cityAbbrMap.get(city))) {
                    House house = new House();
                    house.setCity(city);
                    house.setDistrict(district);
                    house.setUrl(houseUrl);
                    houses.add(house);
                }
            }
        }

        return houses;
    }

    @Override
    public void getHouseInfo(House house) {
        String houseUrl = house.getUrl();
        driver.get(houseUrl);

        StringBuilder sb = new StringBuilder();
        int size = driver.findElements(By.xpath("//a[@class='c-p']")).size();
        if (size == 2) {
            sb.append(driver.findElements(By.xpath("//a[@class='c-p']")).get(1).getText().replaceAll(" |\n", ""));
            sb.append("-");
        }
        sb.append(driver.findElement(By.xpath("//a[@class='ellipsis address']")).getText());
        sb.append("-");
        sb.append(driver.findElement(By.xpath("//a[@class='village-name c-p']")).getText());
        String address = sb.toString();
        String houseType = driver.findElement(By.className("li-house-info-first")).findElement(By.tagName("p")).getText().replaceAll(" |\n", "");

        Float area = null;
        Integer price = null;
        String floor = null;
        String lift = null;
        String orientation = null;
        String buildYear = null;

        List<WebElement> houseElements = driver.findElements(By.xpath("//ul[@class='info-content']/li[@class='info-item']"));
        for (WebElement houseElement: houseElements) {
            String houseInfoLabel = houseElement.findElements(By.tagName("span")).get(0).getText();
            String houseInfoContent = houseElement.findElements(By.tagName("span")).get(1).getText().replaceAll(" |\n", "");

            switch (houseInfoLabel) {
                case "建筑面积：": {
                    String areaStr = houseInfoContent;
                    areaStr = areaStr.substring(0, areaStr.indexOf("㎡"));
                    area = Float.valueOf(areaStr);
                    break;
                }
                case "房屋单价：": {
                    String priceStr = houseInfoContent.substring(0, houseInfoContent.indexOf("元"));
                    price = Integer.valueOf(priceStr);
                    break;
                }
                case "所在楼层：": {
                    floor = houseInfoContent;
                    break;
                }
                case "配备电梯：": {
                    lift = houseInfoContent;
                    break;
                }
                case "房屋朝向：": {
                    orientation = houseInfoContent;
                    break;
                }
                case "建筑年代：": {
                    buildYear = houseInfoContent;
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
        house.setBuildYear(buildYear);
    }


    @Override
    public void setTimeWait(int timeWait) {
        super.setTimeWait(timeWait);
        driver.manage().timeouts().implicitlyWait(timeWait, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {
        return driver;
    }


//    public static void main(String[] args) {
//        CrawlerZgzf crawler = new CrawlerZgzf(10);
//        try{
//            crawler.crawl("杭州",1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            crawler.getDriver().close();
//        }
//    }
}
