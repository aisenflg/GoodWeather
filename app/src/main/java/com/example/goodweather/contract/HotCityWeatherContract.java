package com.example.goodweather.contract;

import android.content.Context;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.WeatherBean;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 热门城市天气订阅器
 */
public class HotCityWeatherContract {

    public static class HotCityWeatherPresenter extends BasePresenter<IHotCityWeatherView>{


        /**
         * 天气数据
         */
        public void hotCityWeather(String location, Context context){
            ApiService mService = ServiceGenerator.createService(ApiService.class,0);
           mService.getWeatherData(location).enqueue(new Callback<WeatherBean>() {
               @Override
               public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
                   if (getView() != null) {
                       getView().getHotCityWeatherResult(response);
                   }
               }

               @Override
               public void onFailure(Call<WeatherBean> call, Throwable t) {
                   if (getView() != null) {
                       getView().getDataFailed();
                   }
               }
           });
        }
    }

    public interface IHotCityWeatherView extends BaseView {

        void getHotCityWeatherResult(Response<WeatherBean> response);

        //错误返回
        void getDataFailed();
    }
}
