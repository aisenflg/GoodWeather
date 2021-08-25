package com.example.goodweather.eventbus;

import com.example.goodweather.bean.WeatherBean;

import java.util.List;

/**
 * 热门城市天气预报事件
 */
public class ForecastEvent {
    public List<WeatherBean.HeWeather6Bean.DailyForecastBean> mForecastBean;


    public ForecastEvent(List<WeatherBean.HeWeather6Bean.DailyForecastBean> forecastBean) {
        this.mForecastBean = forecastBean;
    }

}
