package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class SplashContrat {
    public static class SplashPresenter extends BasePresenter<ISplashView> {
        /**
         * 获取必应  每日一图
         */
        public void biying() {
            ApiService service = NetworkApi.createService(ApiService.class, 1);
            service.biying()
                    .compose(NetworkApi.applySchedulers(new BaseObserver<BiYingImgBean>() {
                        @Override
                        public void onSuccess(BiYingImgBean biYingImgBean) {
                            if (getView() != null) {
                                getView().getBiYingResult(biYingImgBean);
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            if (getView() != null) {
                                getView().getDataFailed();
                            }
                        }
                    }));
//            service.biying().enqueue(new NetCallBack<BiYingImgBean>() {
//                @Override
//                public void onSuccess(Call<BiYingImgBean> call, Response<BiYingImgBean> response) {
//                    if (getView() != null) {
//                        getView().getBiYingResult(response);
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
        }


    }

    public interface ISplashView extends BaseView{

        /**
         * 获取必应每日一图返回
         * @param response BiYingImgResponse
         */
        void getBiYingResult(BiYingImgBean response);

    }
}
