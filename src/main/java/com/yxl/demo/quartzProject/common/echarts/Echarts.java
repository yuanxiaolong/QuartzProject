package com.yxl.demo.quartzProject.common.echarts;

import java.util.ArrayList;
import java.util.List;

public class Echarts {
    public List<String> legend = new ArrayList<String>();//数据分组
    public List<String> axis = new ArrayList<String>();//横坐标
    public List<Series> series = new ArrayList<Series>();//纵坐标

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    public List<String> getAxis() {
        return axis;
    }

    public void setAxis(List<String> axis) {
        this.axis = axis;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    //    public Echarts(List<String> legendList, List<String> categoryList, List<Series> seriesList) {
//        this.legend = legendList;
//        this.axis = categoryList;
//        this.series = seriesList;
//    }
}
