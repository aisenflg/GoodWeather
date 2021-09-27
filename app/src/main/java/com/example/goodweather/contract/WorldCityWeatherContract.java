package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorldCityWeatherContract {
    public static class WorldCityWeatherPresenter extends BasePresenter<IWorldCityWeatherView>{
        ApiService mService = ServiceGenerator.createService(ApiService.class,3);

        /**
         * 实况天气
         */
        public void nowWeather(String location){
            mService.nowWeather(location).enqueue(new Callback<NowResponse>() {
                @Override
                public void onResponse(Call<NowResponse> call, Response<NowResponse> response) {
                    if (getView() != null) {
                        getView().getNowResult(response);
                    }
                }

                @Override
                public void onFailure(Call<NowResponse> call, Throwable t) {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }


        /**
         * 天气预报  V7版本   7d 表示天气的数据 为了和之前看上去差别小一些，这里先用七天的
         * @param location   城市名
         */
        public void dailyWeather(String location){
            mService.dailyWeather("7d",location).enqueue(new Callback<DailyResponse>() {
                @Override
                public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if (getView() != null) {
                        getView().getDailyResult(response);
                    }
                }

                @Override
                public void onFailure(Call<DailyResponse> call, Throwable t) {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 逐小时预报（未来24小时）
         * @param location   城市名
         */
        public void hourlyWeather(String location){
            mService.hourlyWeather(location).enqueue(new Callback<HourlyResponse>() {
                @Override
                public void onResponse(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                    if (getView() != null) {
                        getView().getHourlyResult(response);
                    }
                }

                @Override
                public void onFailure(Call<HourlyResponse> call, Throwable t) {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }
    }

    public interface IWorldCityWeatherView extends BaseView {

        //实况天气
        public void getNowResult(Response<NowResponse> response);
        //天气预报  7天
        void getDailyResult(Response<DailyResponse> response);
        //逐小时天气预报
        void getHourlyResult(Response<HourlyResponse> response);
    }
}
