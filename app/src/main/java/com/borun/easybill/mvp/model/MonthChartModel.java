package com.borun.easybill.mvp.model;

public interface MonthChartModel {

    /**
     * 每月账单图表数据
     */
    void getMonthChartBills(String id, String year, String month);

    void onUnsubscribe();
}
