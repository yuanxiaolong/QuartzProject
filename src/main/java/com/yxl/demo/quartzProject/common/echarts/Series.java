package com.yxl.demo.quartzProject.common.echarts;

import java.util.List;


public class Series {

    public String name;
    public String type;
    public List<Integer> data;

    public Series(String name,String type, List<Integer> data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
