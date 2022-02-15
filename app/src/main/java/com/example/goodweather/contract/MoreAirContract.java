package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.MoreAirFiveResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class MoreAirContract {

    public static class MoreAirPresenter extends BasePresenter<IMoreAirView> {
        ApiService mService = NetworkApi.createService(ApiService.class, 3);

        /**
         * 搜索城市ID
         *
         * @param location
         */
        public void searchCityId(String location) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 4);
//            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
//                @Override
//                public void onSuccess(Call<NewSearchCityResponse> call, NewSearchCityResponse> response) {
//                    if (getView() != null) {
//                        getView().getSearchCityIdResult(response);
//                    }
//                }
//
//                @Override
//                public void onFailed() {
//                    if (getView() != null) {
//                        getView().getDataFailed();
//                    }
//                }
//            });

            ApiService service = NetworkApi.createService(ApiService.class, 4);
            service.newSearchCity(location, "exact")
                    .compose(NetworkApi.applySchedulers(new BaseObserver<NewSearchCityResponse>() {
                        @Override
                        public void onSuccess(NewSearchCityResponse newSearchCityResponse) {
                            if (getView() != null) {
                                getView().getSearchCityIdResult(newSearchCityResponse);
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


        public void air(String location) {
//            mService.airFiveWeather(location).enqueue(new NetCallBack<MoreAirFiveResponse>() {
//                @Override
//                public void onSuccess(Call<MoreAirFiveResponse> call, MoreAirFiveResponse> response) {
//                    if (getView() != null) {
//                        getView().getMoreAirFiveResult(response);
//                    }
//                }
//
//                @Override
//                public void onFailed() {
//                    if (getView() != null) {
//                        getView().getDataFailed();
//                    }
//                }
//            });
            mService.airFiveWeather(location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<MoreAirFiveResponse>() {
                        @Override
                        public void onSuccess(MoreAirFiveResponse moreAirFiveResponse) {
                            if (getView() != null) {
                                getView().getMoreAirFiveResult(moreAirFiveResponse);
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


        /**
         * 五天空气质量预报
         *
         * @param location 城市ID
         */
        public void ariFive(String location) {
//            mService.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
//                @Override
//                public void onSuccess(Call<AirNowResponse> call, AirNowResponse> response) {
//                    if (getView() != null) {
//                        getView().getMoreAirResult(response);
//                    }
//                }
//
//                @Override
//                public void onFailed() {
//                    if (getView() != null) {
//                        getView().getDataFailed();
//                    }
//                }
//            });

            mService.airNowWeather(location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<AirNowResponse>() {
                        @Override
                        public void onSuccess(AirNowResponse airNowResponse) {
                            if (getView() != null) {
                                getView().getMoreAirResult(airNowResponse);
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


    public interface IMoreAirView extends BaseView {
        //搜索城市Id
        void getSearchCityIdResult(NewSearchCityResponse response);

        //空气质量返回数据 V7
        void getMoreAirResult(AirNowResponse response);

        //五天空气质量数据返回 V7
        void getMoreAirFiveResult(MoreAirFiveResponse response);

    }
}
