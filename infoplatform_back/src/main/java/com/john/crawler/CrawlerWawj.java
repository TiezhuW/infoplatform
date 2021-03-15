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
public class CrawlerWawj extends Crawler{
    private WebDriver driver;


    {
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        cityAbbrMap.put("上海", "sh");
        cityAbbrMap.put("杭州", "hz");
        cityAbbrMap.put("南京", "nj");
        cityAbbrMap.put("苏州", "sz");

        System.setProperty("webdriver.edge.driver", "D:\\WebDriver\\msedgedriver.exe");
        driver = new EdgeDriver();

//        System.setProperty("webdriver.chrome.driver", "D:\\WebDriver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);
//        driver = new ChromeDriver(options);
    }


    public CrawlerWawj() {
        super();
    }

    public CrawlerWawj(int timeWait) {
        super(timeWait);
        driver.manage().timeouts().implicitlyWait(timeWait, TimeUnit.SECONDS);
    }


    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        super.setHouseRepository(houseRepository);
    }


    @Override
    public Map<String, String> getDistrictUrlMap(String city) {
        Map<String, String> districtUrlMap = new HashMap<>();
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        String cityAbbr = cityAbbrMap.get(city);
        String cityUrl = "https://" + cityAbbr + ".5i5j.com/ershoufang/";

        driver.get(cityUrl);

        List<WebElement> cityElements = driver.findElements(By.xpath("//a[contains(@title, '二手房')]"));
        for (WebElement cityElement: cityElements) {
            if (!cityElement.getText().equals("海宁") && !cityElement.getText().equals("全部")) {
                districtUrlMap.put(cityElement.getText(), cityElement.getAttribute("href"));
            }
        }

        return districtUrlMap;
    }

    @Override
    public Set<House> getHouseUrls(Map<String, String> districtUrlMap, String city, String district, int pages) {
        Set<House> houses = new HashSet<>();
        String districtUrl = districtUrlMap.get(district);

        for (int page = 1; page <= pages; page++) {
            driver.get(districtUrl + "n" + page);

            List<WebElement> districtElements = driver.findElements(By.xpath("//a[@tdeventid='二手房_列表页_房源']"));
            for (WebElement districtElement: districtElements) {
                String houseUrl = districtElement.getAttribute("href");
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
    public void getHouseInfo(House house) {
        String houseUrl = house.getUrl();

        driver.get(houseUrl);

        Integer price = Math.round(Float.valueOf(driver.findElement(By.xpath("//div[@class='danjia']/span")).getText()) * 10000);
        String region = driver.findElements(By.xpath("//div[@class='zushous']//a[@gio-trace='tracegio_bi']")).get(1).getText();
        String community = driver.findElement(By.xpath("//div[@class='zushous']//a")).getText();
        String address = region + "-" + community;

        Float area = null;
        String floor = null;
        String houseType = null;
        String orientation = null;
        String buildYear = null;

        List<WebElement> houseElements = driver.findElements(By.xpath("//div[@class='saleinfo']//li"));
        for (WebElement houseElement: houseElements) {
            String houseInfoLabel = houseElement.findElement(By.tagName("label")).getText();
            String houseInfoContent = houseElement.findElement(By.tagName("span")).getText();

            switch (houseInfoLabel) {
                case "建筑面积": {
                    area = Float.parseFloat(houseInfoContent.substring(0, houseInfoContent.indexOf("平")));
                    break;
                }
                case "所在楼层": {
                    floor = houseInfoContent;
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
                case "建筑年代": {
                    buildYear = houseInfoContent;
                    break;
                }
            }
        }

        house.setArea(area);
        house.setPrice(price);
        house.setAddress(address);
        house.setFloor(floor);
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
//        CrawlerWawj crawler = new CrawlerWawj(10);
//        try {
//            crawler.crawl("杭州", 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            crawler.getDriver().close();
//        }
//    }
}
