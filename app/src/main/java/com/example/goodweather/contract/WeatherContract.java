package com.example.goodweather.contract;

import android.content.Context;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.AirNowCityBean;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.WeatherBean;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 天气订阅器
 */
public class WeatherContract {

    public static class WeatherPresenter extends BasePresenter<IWeatherView> {
        //和风天气地址
        private ApiService mService = ServiceGenerator.createService(ApiService.class,0);
        //必应地址
        private ApiService mService1 = ServiceGenerator.createService(ApiService.class,1);


        /**
         * 必应每日一图
         */
        public void biying(Context context){
            mService1.biying().enqueue(new NetCallBack<BiYingImgBean>() {
                @Override
                public void onSuccess(Call<BiYingImgBean> call, Response<BiYingImgBean> response) {
                    if (getView() != null) {
                        getView().getBiYingResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }


        /**
         *
         * @param context
         * @param location
         * 空气质量数据
         */
        public void getAirNowCityResult(Context context, String location){
            mService.getAirNowCity(location).enqueue(new NetCallBack<AirNowCityBean>() {
                @Override
                public void onSuccess(Call<AirNowCityBean> call, Response<AirNowCityBean> response) {
                    getView().getAirNowCityResult(response);
                }

                @Override
                public void onFailed() {
                    getView().getDataFailed();
                }
            });
        }

        public void getWeatherResult(Context context,String location){
           mService.getWeatherData(location).enqueue(new NetCallBack<WeatherBean>() {
               @Override
               public void onSuccess(Call<WeatherBean> call, Response<WeatherBean> response) {
                   getView().getWeatherResult(response);
               }

               @Override
               public void onFailed() {
                    getView().getDataFailed();
               }
           });
        }

    }



    public interface IWeatherView extends BaseView {
        //必应每日意图返回
        void getBiYingResult(Response<BiYingImgBean> response);

        //空气质量数据
        void getAirNowCityResult(Response<AirNowCityBean> response);
        //天气所有数据
        void getWeatherResult(Response<WeatherBean> response);
        //天气数据错误返回
        void getWeatherDataFailed();
        //错误返回
        void getDataFailed();
    }

}
