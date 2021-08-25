package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.HotCityBean;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 热门城市订阅器
 */
public class HotCityContract {

    public static class HotCityPresenter extends BasePresenter<IHotCityView>{


        public void hotCity(){
            ApiService mService = ServiceGenerator.createService(ApiService.class,2);
            mService.hotCity().enqueue(new Callback<HotCityBean>() {
                @Override
                public void onResponse(Call<HotCityBean> call, Response<HotCityBean> response) {
                    if (getView() != null) {
                        getView().getHotCityResult(response);
                    }

                }

                @Override
                public void onFailure(Call<HotCityBean> call, Throwable t) {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }
    }

    public interface IHotCityView extends BaseView {

        void getHotCityResult(Response<HotCityBean> response);

        //错误返回
        void getDataFailed();
    }
}
