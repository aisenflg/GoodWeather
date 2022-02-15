package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.goodweather.bean.SunMoonResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class MapWeatherContract {
    public static class MapWeatherPresenter extends BasePresenter<IMapWeatherView> {
        /**
         * 搜索城市  V7版本中  需要把定位城市的id查询出来，然后通过这个id来查询详细的数据
         *
         * @param location 城市名
         */
        public void searchCity(String location) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//访问地址
//            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
//                @Override
//                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
//                    if (getView() != null) {
//                        getView().getNewSearchCityResult(response);
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
            ApiService service = NetworkApi.createService(ApiService.class, 4);//访问地址
            service.newSearchCity(location, "exact")
                    .compose(NetworkApi.applySchedulers(new BaseObserver<NewSearchCityResponse>() {

                        @Override
                        public void onSuccess(NewSearchCityResponse newSearchCityResponse) {
                            if (getView() != null) {
                                getView().getNewSearchCityResult(newSearchCityResponse);
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
         * 实况天气  V7版本
         *
         * @param location 城市名
         */
        public void nowWeather(String location) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 3);//访问地址
//            service.nowWeather(location).enqueue(new NetCallBack<NowResponse>() {
//                @Override
//                public void onSuccess(Call<NowResponse> call, Response<NowResponse> response) {
//                    if (getView() != null) {
//                        getView().getNowResult(response);
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
            ApiService service = NetworkApi.createService(ApiService.class, 3);//访问地址
            service.nowWeather(location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<NowResponse>() {
                        @Override
                        public void onSuccess(NowResponse nowResponse) {
                            if (getView() != null) {
                                getView().getNowResult(nowResponse);
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
         * 天气预报  V7版本   7d 表示天气的数据 为了和之前看上去差别小一些，这里先用七天的
         *
         * @param location 城市名
         */
        public void dailyWeather(String location) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 3);//访问地址
//            service.dailyWeather("7d", location).enqueue(new NetCallBack<DailyResponse>() {
//                @Override
//                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
//                    if (getView() != null) {
//                        getView().getDailyResult(response);
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

            ApiService service = NetworkApi.createService(ApiService.class, 3);//访问地址
            service.dailyWeather("7d", location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<DailyResponse>() {
                        @Override
                        public void onSuccess(DailyResponse dailyResponse) {
                            if (getView() != null) {
                                getView().getDailyResult(dailyResponse);
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

        public void airNowWeather(String location) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 3);//访问地址
//            service.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
//                @Override
//                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
//                    if (getView() != null) {
//                        getView().getAirNowResult(response);
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

            ApiService service = NetworkApi.createService(ApiService.class, 3);//访问地址
            service.airNowWeather(location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<AirNowResponse>() {
                        @Override
                        public void onSuccess(AirNowResponse airNowResponse) {
                            if (getView() != null) {
                                getView().getAirNowResult(airNowResponse);
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
         * 24小时天气预报
         *
         * @param location 城市名
         */
        public void weatherHourly(String location) {//这个3 表示使用新的V7API访问地址
//            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
//            service.hourlyWeather(location).enqueue(new NetCallBack<HourlyResponse>() {
//                @Override
//                public void onSuccess(Call<HourlyResponse> call, Response<HourlyResponse> response) {
//                    if (getView() != null) {
//                        getView().getWeatherHourlyResult(response);
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
            ApiService service = NetworkApi.createService(ApiService.class, 3);
            service.hourlyWeather(location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<HourlyResponse>() {
                        @Override
                        public void onSuccess(HourlyResponse hourlyResponse) {
                            if (getView() != null) {
                                getView().getWeatherHourlyResult(hourlyResponse);
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
         * 日出日落、月升月落
         *
         * @param location 城市名
         */
        public void getSunMoon(String location, String date) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
//            service.getSunMoon(location, date).enqueue(new NetCallBack<SunMoonResponse>() {
//                @Override
//                public void onSuccess(Call<SunMoonResponse> call, Response<SunMoonResponse> response) {
//                    if (getView() != null) {
//                        getView().getSunMoonResult(response);
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
            ApiService service = NetworkApi.createService(ApiService.class, 3);
            service.getSunMoon(location, date)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<SunMoonResponse>() {
                        @Override
                        public void onSuccess(SunMoonResponse sunMoonResponse) {
                            if (getView() != null) {
                                getView().getSunMoonResult(sunMoonResponse);
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


    public interface IMapWeatherView extends BaseView {
        //搜索城市返回城市id  通过id才能查下面的数据,否则会提示400  V7
        void getNewSearchCityResult(NewSearchCityResponse response);

        //实况天气
        void getNowResult(NowResponse response);

        //天气预报  7天
        void getDailyResult(DailyResponse response);

        //空气质量
        void getAirNowResult(AirNowResponse response);

        //太阳和月亮
        void getSunMoonResult(SunMoonResponse response);

        //24小时天气预报
        void getWeatherHourlyResult(HourlyResponse response);

    }
}
