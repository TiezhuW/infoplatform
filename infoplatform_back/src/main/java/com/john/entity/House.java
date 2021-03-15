package com.john.entity;

import lombok.Data;

@Data
public class House {
    private Integer id;
    private String city;        //市
    private String district;    //区
    private Float area;         //面积
    private Integer price;      //价格
    private String address;     //地址
    private String floor;       //楼层
    private String lift;        //电梯
    private String houseType;   //户型
    private String orientation; //朝向
    private String buildYear;   //建造年代
    private String url;         //链接
}
