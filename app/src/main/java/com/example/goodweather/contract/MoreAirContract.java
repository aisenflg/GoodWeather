package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.MoreAirFiveResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class MoreAirContract {

    public static class MoreAirPresenter extends BasePresenter<IMoreAirView>{
        ApiService mService = ServiceGenerator.createService(ApiService.class,3);

        /**
         * 搜索城市ID
         * @param location
         */
        public void searchCityId(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,4);
            service.newSearchCity(location,"exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getSearchCityIdResult(response);
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


        public void air(String location){
            mService.airFiveWeather(location).enqueue(new NetCallBack<MoreAirFiveResponse>() {
                @Override
                public void onSuccess(Call<MoreAirFiveResponse> call, Response<MoreAirFiveResponse> response) {
                    if (getView() != null) {
                        getView().getMoreAirFiveResult(response);
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
         * 五天空气质量预报
         * @param location  城市ID
         */
        public void ariFive(String location){
            mService.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if (getView() != null) {
                        getView().getMoreAirResult(response);
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
    }



    public interface IMoreAirView extends BaseView{
        //搜索城市Id
        void getSearchCityIdResult(Response<NewSearchCityResponse> response);

        //空气质量返回数据 V7
        void getMoreAirResult(Response<AirNowResponse> response);

        //五天空气质量数据返回 V7
        void getMoreAirFiveResult(Response<MoreAirFiveResponse> response);

    }
}
