package com.yxl.demo.quartzProject.util;

import com.yxl.demo.quartzProject.common.echarts.Echarts;
import com.yxl.demo.quartzProject.common.echarts.PointDTO;
import com.yxl.demo.quartzProject.common.echarts.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EchartsParser {

    //获取横坐标
    public static List<String> getXAxis(String beginTime, String endTime, Integer period){
        List<String> list = new ArrayList<String>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date begin = df.parse(beginTime);
            Date end = df.parse(endTime);
            while (begin.getTime()<end.getTime()){
                list.add(df.format(begin));
                begin=new Date(begin.getTime() + period * 60 * 1000);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Echarts parseEcharts(List<PointDTO> list, String beginTime, String endTime, Integer period, String chartType){
        Echarts echarts=new Echarts();

//        if (list!=null && list.size()>0){
            List<String> legend=new ArrayList<String>();
            List<Series> series=new ArrayList<Series>();
            List<String> axis=getXAxis(beginTime,endTime,period);

            String lineName="";
            ArrayList<Integer> data= new ArrayList<Integer>();
            int gap = 0;//数据间隙
            for(PointDTO dto:list){
                if ((!lineName.equals(dto.getLineName())) && (!lineName.equals(""))){
                    // 补齐数据结尾的间隙
                    gap=axis.size()-data.size();
                    for (int i = 0; i < gap; i++) {
                        data.add(0);
                    }
                    legend.add(lineName);
                    series.add(new Series(lineName,chartType,data));
                    data = new ArrayList<Integer>();
                }
                int index=axis.indexOf(dto.getSendTimestamp());
                // 补齐数据中间的间隙
                gap=index-data.size();
                for (int i = 0; i < gap; i++) {
                    data.add(0);
                }
                data.add(dto.getTotalRows().intValue());
                lineName=dto.getLineName();
            }
            // 补齐数据结尾的间隙
            gap=axis.size()-data.size();
            for (int i = 0; i < gap; i++) {
                data.add(0);
            }
            legend.add(lineName);
            series.add(new Series(lineName,chartType,data));
            echarts.setLegend(legend);
            echarts.setAxis(axis);
            echarts.setSeries(series);
//        }
        return echarts;
    }
}
