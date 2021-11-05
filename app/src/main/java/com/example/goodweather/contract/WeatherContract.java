package com.example.goodweather.contract;

import android.content.Context;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.LifestyleResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.goodweather.bean.WarningResponse;
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
        //V7 版本天气查询
        private ApiService mService = ServiceGenerator.createService(ApiService.class,3);

        /**
         * 必应每日一图
         */
        public void biying(Context context){
            //必应地址
            ApiService service = ServiceGenerator.createService(ApiService.class,1);
            service.biying().enqueue(new NetCallBack<BiYingImgBean>() {
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
         * 搜索城市  V7版本中  需要把定位城市的id查询出来，然后通过这个id来查询详细的数据
         * @param location 城市名
         */
        public void newSearchCity(String location) {//注意这里的4表示新的搜索城市地址接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.newSearchCity(location,"exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if(getView() != null){
                        getView().getNewSearchCityResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }


        /**
         * 实况天气  V7版本
         * @param location  城市名
         */
        public void nowWeather(String location){//这个3 表示使用新的V7API访问地址
            mService.nowWeather(location).enqueue(new NetCallBack<NowResponse>() {
                @Override
                public void onSuccess(Call<NowResponse> call, Response<NowResponse> response) {
                    if(getView() != null){
                        getView().getNowResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 天气预报  V7版本   7d 表示天气的数据 为了和之前看上去差别小一些，这里先用七天的
         * @param location   城市名
         */
        public void dailyWeather(String location){//这个3 表示使用新的V7API访问地址
            mService.dailyWeather("7d",location).enqueue(new NetCallBack<DailyResponse>() {
                @Override
                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if(getView() != null){
                        getView().getDailyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 逐小时预报（未来24小时）
         * @param location   城市名
         */
        public void hourlyWeather(String location){
            mService.hourlyWeather(location).enqueue(new NetCallBack<HourlyResponse>() {
                @Override
                public void onSuccess(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                    if(getView() != null){
                        getView().getHourlyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 当天空气质量
         * @param location   城市名
         */
        public void airNowWeather(String location){

            mService.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if(getView() != null){
                        getView().getAirNowResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 生活指数
         * @param location   城市名  type中的"1,2,3,5,6,8,9,10"，表示只获取这8种类型的指数信息，同样是为了对应之前的界面UI
         */
        public void lifestyle(String location){
            mService.lifestyle("1,2,3,5,6,8,9,10",location).enqueue(new NetCallBack<LifestyleResponse>() {
                @Override
                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
                    if(getView() != null){
                        getView().getLifestyleResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        public void warning(String location){
            mService.nowWarning(location).enqueue(new NetCallBack<WarningResponse>() {
                @Override
                public void onSuccess(Call<WarningResponse> call, Response<WarningResponse> response) {
                    if (getView() != null) {
                        getView().getWarningResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }


    }



    public interface IWeatherView extends BaseView {
        //必应每日意图返回
        void getBiYingResult(Response<BiYingImgBean> response);


        /*                以下为V7版本新增               */

        //搜索城市返回城市id  通过id才能查下面的数据,否则会提示400  V7
        void getNewSearchCityResult(Response<NewSearchCityResponse> response);
        //实况天气
        void getNowResult(Response<NowResponse> response);
        //天气预报  7天
        void getDailyResult(Response<DailyResponse> response);
        //逐小时天气预报
        void getHourlyResult(Response<HourlyResponse> response);
        //空气质量
        void getAirNowResult(Response<AirNowResponse> response);
        //生活指数
        void getLifestyleResult(Response<LifestyleResponse> response);
        //灾害预警
        void getWarningResult(Response<WarningResponse> response);

        //天气数据错误返回
        void getWeatherDataFailed();

    }

}
