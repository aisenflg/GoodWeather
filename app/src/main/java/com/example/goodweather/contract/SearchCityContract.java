package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;


/**
 * 搜索历史订阅器
 */
public class SearchCityContract {

    public static class SearchCityPresenter extends BasePresenter<ISearchCityView> {



        public void newSearchCity(String location){
//            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
//            service.newSearchCity(location,"fuzzy").enqueue(new Callback<NewSearchCityResponse>() {
//                @Override
//                public void onResponse(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
//                    if(getView() != null){
//                        getView().getNewSearchCityResult(response);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<NewSearchCityResponse> call, Throwable t) {
//                    if(getView() != null){
//                        getView().getDataFailed();
//                    }
//                }
//            });

            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.newSearchCity(location,"fuzzy")
                    .compose(NetworkApi.applySchedulers(new BaseObserver<NewSearchCityResponse>() {
                        @Override
                        public void onSuccess(NewSearchCityResponse newSearchCityResponse) {
                            if (getView() != null) {
                                getView().getNewSearchCityResult(newSearchCityResponse);
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


  public interface ISearchCityView extends BaseView{

      //搜索城市返回数据  V7
      void getNewSearchCityResult(NewSearchCityResponse response);


    }
}
