package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.LifestyleResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class MoreLifestyleContract {
    public static class MoreLifestylePresenter extends BasePresenter<IMoreLifestyleView>{
        ApiService service = ServiceGenerator.createService(ApiService.class,3);

        /**
         * 更多生活指数
         * @param location
         */
        public void worldCity(String location) {
            service.lifestyle("0",location).enqueue(new NetCallBack<LifestyleResponse>() {
                @Override
                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
                    if (getView() != null) {
                        getView().getMoreLifestyleResult(response);
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

    public interface IMoreLifestyleView extends BaseView{
        //更多生活指数返回数据 V7
        void getMoreLifestyleResult(Response<LifestyleResponse> response);
    }
}
