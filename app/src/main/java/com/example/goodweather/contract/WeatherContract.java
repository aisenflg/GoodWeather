package com.example.goodweather.contract;

import android.content.Context;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.AirNowCityBean;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.HourlyBean;
import com.example.goodweather.bean.LifeStyleBean;
import com.example.goodweather.bean.TodayBean;
import com.example.goodweather.bean.WeatherForecastBean;
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
         * 当日天气
         *
         * @param context
         * @param location 区/县
         */
        public void todayWeather(Context context, String location) {
            //设置请求回调  NetCallBack是重写请求回调
            mService.getTodayWeather(location).enqueue(new NetCallBack<TodayBean>() {
                @Override
                public void onSuccess(Call<TodayBean> call, Response<TodayBean> response) {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getTodayWeatherResult(response);

                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getDataFailed();

                    }
                }
            });
        }

        /**
         * 天气预报  3-7天(白嫖的就只能看到3天)
         * @param context
         * @param location
         */
        public void weatherForecast(Context context, String location){
            //设置请求回调  NetCallBack是重写请求回调
            mService.getWeatherForecast(location).enqueue(new NetCallBack<WeatherForecastBean>() {
                @Override
                public void onSuccess(Call<WeatherForecastBean> call, Response<WeatherForecastBean> response) {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getWeatherForecastResult(response);

                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getDataFailed();

                    }
                }
            });
        }
        /**
         * 生活指数
         * @param context
         * @param location
         */
        public void lifeStyle(Context context, String location){
            mService.getLifeStyle(location).enqueue(new NetCallBack<LifeStyleBean>() {
                @Override
                public void onSuccess(Call<LifeStyleBean> call, Response<LifeStyleBean> response) {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getLifeStyleResult(response);

                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getDataFailed();

                    }
                }
            });
        }

        /**
         * 必应每日一图
         */
        public void biying(Context context){
            mService1.biying().enqueue(new NetCallBack<BiYingImgBean>() {
                @Override
                public void onSuccess(Call<BiYingImgBean> call, Response<BiYingImgBean> response) {
                    getView().getBiYingResult(response);
                }

                @Override
                public void onFailed() {
                    getView().getDataFailed();
                }
            });
        }

        /**
         * 逐小时天气预报数据
         * @param context
         * @param location
         */
        public void getHourly(Context context, String location){
            mService.getHourly(location).enqueue(new NetCallBack<HourlyBean>() {
                @Override
                public void onSuccess(Call<HourlyBean> call, Response<HourlyBean> response) {
                    getView().getHourly(response);
                }

                @Override
                public void onFailed() {
                    getView().getDataFailed();
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


    }



    public interface IWeatherView extends BaseView {
        //查询当天天气的数据返回
        void getTodayWeatherResult(Response<TodayBean> response);

        //查询天气预报的数据返回
        void getWeatherForecastResult(Response<WeatherForecastBean> response);
        //查询生活指数数据返回
        void getLifeStyleResult(Response<LifeStyleBean> response);

        //必应每日意图返回
        void getBiYingResult(Response<BiYingImgBean> response);
        //逐小时预报
        void getHourly(Response<HourlyBean> response);
        //空气质量数据
        void getAirNowCityResult(Response<AirNowCityBean> response);

        //错误返回
        void getDataFailed();
    }

}
