package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.DailyResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class MoreDailyContract {
    public static class MoreDailyPresenter extends BasePresenter<IMoreDailyView>{

        /**
         * 更多天气预报 V7
         * @param location
         */
        public void moreDaily(String location){
//            ApiService service  = ServiceGenerator.createService(ApiService.class,3);
//            service.dailyWeather("15d",location).enqueue(new Callback<DailyResponse>() {
//                @Override
//                public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {
//                    if (getView() != null) {
//                        getView().getMoreDailyResult(response);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<DailyResponse> call, Throwable t) {
//                    if (getView() != null) {
//                        getView().getDataFailed();
//                    }
//                }
//            });

            ApiService service  = NetworkApi.createService(ApiService.class,3);
            service.dailyWeather("15d",location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<DailyResponse>() {
                        @Override
                        public void onSuccess(DailyResponse dailyResponse) {
                            if (getView() != null) {
                                getView().getMoreDailyResult(dailyResponse);
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            if (getView() != null) {
                                getView().getDataFailed();
                            }
                        }
                    }));

        }

    }

    public interface IMoreDailyView extends BaseView{
        //更多天气预报返回数据 V7
        void getMoreDailyResult(DailyResponse response);
    }
}
