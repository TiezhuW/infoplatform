package com.john.controller;

import com.john.entity.Collect;
import com.john.entity.House;
import com.john.entity.Result;
import com.john.service.CollectService;
import com.john.service.HouseService;
import com.john.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;
    @Autowired
    private UserService userService;
    @Autowired
    private HouseService houseService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/findByUserId/{userId}")
    public List<House> findByUserId(@PathVariable("userId") Integer userId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer currentUserId = userService.findByUsername(username).getId();
        List<House> houses = new ArrayList<>();
        if (currentUserId.equals(userId)) {
            List<Collect> collects = collectService.findByUserId(userId);
            List<Integer> houseIds = new ArrayList<>();
            for (Collect collect: collects) {
                houseIds.add(collect.getHouseId());
            }
            for (Integer houseId: houseIds) {
                House house = houseService.findById(houseId);
                houses.add(house);
            }
        }
        return houses;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/save/{houseId}")
    public Result save(@PathVariable("houseId") Integer houseId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userService.findByUsername(username).getId();
        Collect collect = new Collect(userId, houseId);
        int ret;
        try {
            ret = collectService.save(collect);
        } catch (Exception e) {
            ret = 0;
        }
        if (ret == 1) {
            return Result.success("收藏成功");
        } else {
            return Result.fail("收藏失败");
        }
    }

    @DeleteMapping("/delete/{houseId}")
    public Result delete(@PathVariable("houseId") Integer houseId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userService.findByUsername(username).getId();
        Collect collect = new Collect(userId, houseId);

        int ret;
        try{
            ret = collectService.delete(collect);
        } catch (Exception e) {
            ret = 0;
        }
        if (ret == 1) {
            return Result.success("收藏删除成功");
        } else {
            return Result.fail("收藏删除失败");
        }
    }
}
