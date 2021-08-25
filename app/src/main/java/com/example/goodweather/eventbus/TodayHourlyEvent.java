package com.example.goodweather.eventbus;

import com.example.goodweather.bean.WeatherBean;

import java.util.List;

/**
 * 热门城市当天逐三小时天气信息事件
 */
public class TodayHourlyEvent {
    public List<WeatherBean.HeWeather6Bean.HourlyBean> mHourlyBean;


    public TodayHourlyEvent(List<WeatherBean.HeWeather6Bean.HourlyBean> hourlyBean) {
        this.mHourlyBean = hourlyBean;
    }

}
