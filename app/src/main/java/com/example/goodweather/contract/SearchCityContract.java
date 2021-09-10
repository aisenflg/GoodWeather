package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 搜索历史订阅器
 */
public class SearchCityContract {

    public static class SearchCityPresenter extends BasePresenter<ISearchCityView> {
        ApiService mService = ServiceGenerator.createService(ApiService.class, 2);


        public void newSearchCity(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.newSearchCity(location,"fuzzy").enqueue(new Callback<NewSearchCityResponse>() {
                @Override
                public void onResponse(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if(getView() != null){
                        getView().getNewSearchCityResult(response);
                    }
                }

                @Override
                public void onFailure(Call<NewSearchCityResponse> call, Throwable t) {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

    }


  public interface ISearchCityView extends BaseView{

      //搜索城市返回数据  V7
      void getNewSearchCityResult(Response<NewSearchCityResponse> response);

      //错误返回
      void getDataFailed();
    }
}
