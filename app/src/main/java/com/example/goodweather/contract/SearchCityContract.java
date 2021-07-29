package com.example.goodweather.contract;

import android.content.Context;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.SearchCityBean;
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

        public void searchCity(Context context, String location){
            mService.searchCity(location).enqueue(new Callback<SearchCityBean>() {
                @Override
                public void onResponse(Call<SearchCityBean> call, Response<SearchCityBean> response) {
                    if (getView() != null) {
                        getView().getSearchCityResult(response);
                    }
                }

                @Override
                public void onFailure(Call<SearchCityBean> call, Throwable t) {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }
    }


  public interface ISearchCityView extends BaseView{
      //查询城市返回数据
      void getSearchCityResult(Response<SearchCityBean> response);
      //错误返回
      void getDataFailed();
    }
}
