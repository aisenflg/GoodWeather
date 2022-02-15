package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.WorldCityResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class WorldCityContract {
    public static class WorldCityPresenter extends BasePresenter<IWorldCityView>{
        /**
         * 世界城市  V7
         * @param range  类型
         */
        public void worldCity(String range) {
//            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
//            service.worldCity(range).enqueue(new Callback<WorldCityResponse>() {
//                @Override
//                public void onResponse(Call<WorldCityResponse> call, Response<WorldCityResponse> response) {
//                    if(getView() != null){
//                        getView().getWorldCityResult(response);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<WorldCityResponse> call, Throwable t) {
//                    if(getView() != null){
//                        getView().getDataFailed();
//                    }
//                }
//            });

            ApiService service = NetworkApi.createService(ApiService.class, 4);
            service.worldCity(range)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<WorldCityResponse>() {
                        @Override
                        public void onSuccess(WorldCityResponse response) {
                            if (getView() != null) {
                                getView().getWorldCityResult(response);
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

    public interface IWorldCityView extends BaseView{
        //热门城市返回数据 V7
        void getWorldCityResult(WorldCityResponse response);

    }

}
