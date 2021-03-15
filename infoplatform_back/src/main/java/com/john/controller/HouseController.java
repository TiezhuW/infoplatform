package com.john.controller;

import com.github.pagehelper.PageInfo;
import com.john.entity.House;
import com.john.entity.Result;
import com.john.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/house")
public class HouseController {
    @Autowired
    HouseService houseService;

    @GetMapping(value = "/findByCityDistrict/{city}/{district}/{pageNum}/{pageSize}")
    public PageInfo findByCityDistrict(@PathVariable("city") String city,
                                       @PathVariable("district") String district,
                                       @PathVariable("pageNum") int pageNum,
                                       @PathVariable("pageSize") int pageSize,
                                       @RequestParam(value = "minArea",required = false) Float minArea,
                                       @RequestParam(value = "maxArea",required = false) Float maxArea,
                                       @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                       @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                       @RequestParam(value = "order") String order) {
        return houseService.findByCityDistrict(city, district, pageNum, pageSize, minArea, maxArea, minPrice, maxPrice, order);
    }

    @GetMapping("/findById/{id}")
    public House findById(@PathVariable("id") Integer id) {
        return houseService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public Result save(@RequestBody House house) {
        int ret;
        try {
            ret = houseService.save(house);
        } catch (Exception e) {
            ret = 0;
        }
        if (ret == 1) {
            return Result.success("添加成功");
        } else {
            return Result.fail("添加失败");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public Result update(@RequestBody House house) {
        int ret;
        try{
            ret = houseService.update(house);
        } catch (Exception e) {
            ret = 0;
        }
        if (ret == 1) {
            return Result.success("二手房信息修改成功");
        } else {
            return Result.fail("二手房信息修改失败");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id") Integer id) {
        int ret;
        try{
            ret = houseService.deleteById(id);
        } catch (Exception e) {
            ret = 0;
        }
        if (ret == 1) {
            return Result.success("二手房信息删除成功");
        } else {
            return Result.fail("二手房信息删除失败");
        }
    }

    /**
     * 启动爬虫引擎
     * @param platform  二手房信息爬虫源平台名
     * @param city      城市名
     * @param pages     此城市各区域爬取条目的页数
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crawl/{platform}/{city}/{pages}")
    public Result crawl(@PathVariable("platform") String platform , @PathVariable("city") String city, @PathVariable("pages") int pages) {
        try {
            switch (platform) {
                case "安居客":
                    houseService.crawlAjk(city, pages);
                    break;
                case "贝壳":
                    houseService.crawlBk(city, pages);
                    break;
                case "房多多":
                    houseService.crawlFdd(city, pages);
                    break;
                case "房天下":
                    houseService.crawlFtx(city, pages);
                    break;
                case "我爱我家":
                    houseService.crawlWawj(city, pages);
                    break;
                case "诸葛找房":
                    houseService.crawlZgzf(city, pages);
                    break;
                default:
                    return Result.fail("暂不支持爬取该平台数据");
            }
        } catch (Exception e) {
            Result.fail("爬取过程中出现异常");
        }
        return Result.success("完成爬取");
    }
}
