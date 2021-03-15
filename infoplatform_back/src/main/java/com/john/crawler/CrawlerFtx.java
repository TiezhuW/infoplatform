package com.john.crawler;

import com.john.repository.HouseRepository;
import com.john.entity.House;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CrawlerFtx extends Crawler{
    private WebDriver driver;


    {
        Map<String, String> cityAbbrMap = getCityAbbrMap();
        cityAbbrMap.put("上海", "sh");
        cityAbbrMap.put("杭州", "hz");
        cityAbbrMap.put("南京", "nanjing");
        cityAbbrMap.put("苏州", "suzhou");

        System.setProperty("webdriver.edge.driver", "D:\\WebDriver\\msedgedriver.exe");
        driver = new EdgeDriver();

//        System.setProperty("webdriver.chrome.driver", "D:\\WebDriver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);
//        driver = new ChromeDriver(options);
    }


    public CrawlerFtx() {
        super();
    }

    public CrawlerFtx(int timeWait) {
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
        String cityUrl = "https://" + cityAbbr + ".esf.fang.com";

        driver.get(cityUrl);

        List<WebElement> cityElements = driver.findElements(By.xpath("//ul[@class='clearfix choose_screen floatl']")).get(0).findElements(By.tagName("a"));
        for (WebElement cityElement: cityElements) {
            if (!cityElement.getText().equals("杭州周边")) {
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
            driver.get(districtUrl + "i3" + page);

            List<WebElement> districtElements = driver.findElements(By.xpath("//h4[@class='clearfix']/a"));
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

        WebDriverWait wait1 = new WebDriverWait(driver, 10);
        wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='rcont']")));
        String region = driver.findElement(By.id("address")).findElements(By.tagName("a")).get(1).getText().replaceAll(" |\n", "");
        String community = driver.findElements(By.xpath("//div[@class='rcont']")).get(0).findElement(By.tagName("a")).getText().trim();
        String address = region + "-" + community;

        Float area = null;
        Integer price = null;
        String floor = null;
        String houseType = null;
        String orientation = null;

        List<WebElement> houseInfoLabels = driver.findElements(By.xpath("//div[@class='font14']"));
        List<WebElement> houseInfoContents = driver.findElements(By.xpath("//div[@class='tt']"));
        for (int i = 0; i < houseInfoLabels.size(); i++) {
            String houseInfoLabel = houseInfoLabels.get(i).getAttribute("innerHTML");
            String houseInfoContent = houseInfoContents.get(i).getAttribute("innerHTML");

            switch (houseInfoLabel) {
                case "建筑面积": {
                    String areaStr = houseInfoContent;
                    area = Float.valueOf(areaStr.substring(0, areaStr.indexOf("平")));
                    break;
                }
                case "单价": {
                    String priceStr = houseInfoContent.trim();
                    price = Integer.valueOf(priceStr.substring(0, priceStr.indexOf("元")));
                    break;
                }
                case "户型": {
                    houseType = houseInfoContent.replaceAll(" |\n", "");
                    break;
                }
                case "朝向": {
                    orientation = houseInfoContent;
                    break;
                }
            }

            if (houseInfoLabel.contains("楼层")) {
                floor = houseInfoContent + houseInfoLabel.substring(2);
            }
        }

        String lift = null;
        String buildYear = null;

        List<WebElement> houseElements2 = driver.findElements(By.xpath("//div[@class='cont clearfix']/div[@class='text-item clearfix']"));
        for (WebElement houseElement: houseElements2) {
            String houseInfoLabel = houseElement.findElements(By.tagName("span")).get(0).getText();
            String houseInfoContent = houseElement.findElements(By.tagName("span")).get(1).getText();

            switch (houseInfoLabel) {
                case "有无电梯": {
                    lift = houseInfoContent;
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
//        CrawlerFtx crawler = new CrawlerFtx(14);
//        try {
//            crawler.crawl("杭州", 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            crawler.getDriver().close();
//        }
//    }
}
