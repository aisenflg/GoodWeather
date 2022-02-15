package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.LifestyleResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class MoreLifestyleContract {
    public static class MoreLifestylePresenter extends BasePresenter<IMoreLifestyleView>{
        ApiService service = NetworkApi.createService(ApiService.class,3);

        /**
         * 更多生活指数
         * @param location
         */
        public void worldCity(String location) {
//            service.lifestyle("0",location).enqueue(new NetCallBack<LifestyleResponse>() {
//                @Override
//                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
//                    if (getView() != null) {
//                        getView().getMoreLifestyleResult(response);
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

            service.lifestyle("0",location)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<LifestyleResponse>() {
                        @Override
                        public void onSuccess(LifestyleResponse lifestyleResponse) {
                            if (getView() != null) {
                                getView().getMoreLifestyleResult(lifestyleResponse);
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

    public interface IMoreLifestyleView extends BaseView{
        //更多生活指数返回数据 V7
        void getMoreLifestyleResult(LifestyleResponse response);
    }
}
