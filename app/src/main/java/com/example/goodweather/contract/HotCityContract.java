package com.example.goodweather.contract;

import android.content.Context;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.HotCityBean;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

/**
 * 热门城市订阅器
 */
public class HotCityContract {

    public static class HotCityPresenter extends BasePresenter<IHotCityView> {

        public void hotCity(Context context, String group) {
//            ApiService mService = ServiceGenerator.createService(ApiService.class,2);
//            mService.hotCity(group).enqueue(new Callback<HotCityBean>() {
//                @Override
//                public void onResponse(Call<HotCityBean> call, Response<HotCityBean> response) {
//                    if (getView() != null) {
//                        getView().getHotCityResult(response);
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<HotCityBean> call, Throwable t) {
//                    if (getView() != null) {
//                        getView().getDataFailed();
//                    }
//                }
//            });

            ApiService apiService = NetworkApi.createService(ApiService.class, 2);
            apiService.hotCity(group)
                    .compose(NetworkApi.applySchedulers(new BaseObserver<HotCityBean>() {
                        @Override
                        public void onSuccess(HotCityBean hotCityBean) {
                            if (getView() != null) {
                                getView().getHotCityResult(hotCityBean);
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

    public interface IHotCityView extends BaseView {

        void getHotCityResult(HotCityBean response);

    }
}
