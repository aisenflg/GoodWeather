package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class SplashContrat {
    public static class SplashPresenter extends BasePresenter<ISplashView> {
        /**
         * 获取必应  每日一图
         */
        public void biying() {
            ApiService service = ServiceGenerator.createService(ApiService.class, 1);
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


    }

    public interface ISplashView extends BaseView{

        /**
         * 获取必应每日一图返回
         * @param response BiYingImgResponse
         */
        void getBiYingResult(Response<BiYingImgBean> response);

    }
}
