package com.yxl.demo.quartzProject.common.echarts;

/**
 * 点
 * author: xiaolong.yuanxl
 * date: 2015-06-05 下午1:14
 */
public class PointDTO {

    private String lineName;

    private String sendTimestamp;

    private Long   totalRows;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(String sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        this.totalRows = totalRows;
    }
}
