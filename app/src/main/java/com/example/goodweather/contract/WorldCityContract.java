package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.WorldCityResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorldCityContract {
    public static class WorldCityPresenter extends BasePresenter<IWorldCityView>{
        /**
         * 世界城市  V7
         * @param range  类型
         */
        public void worldCity(String range) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.worldCity(range).enqueue(new Callback<WorldCityResponse>() {
                @Override
                public void onResponse(Call<WorldCityResponse> call, Response<WorldCityResponse> response) {
                    if(getView() != null){
                        getView().getWorldCityResult(response);
                    }
                }

                @Override
                public void onFailure(Call<WorldCityResponse> call, Throwable t) {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

    }

    public interface IWorldCityView extends BaseView{
        //热门城市返回数据 V7
        void getWorldCityResult(Response<WorldCityResponse> response);

    }

}
