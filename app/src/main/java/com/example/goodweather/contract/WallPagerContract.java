package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.WallPaperResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class WallPagerContract {
    public static class WallPagerPresenter extends BasePresenter<IWallPagerView>{
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


        /**
         * 获取壁纸数据
         */
        public void getWallPaper() {
            // 6 表示访问网络壁纸接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 6);
            service.getWallPaper().enqueue(new NetCallBack<WallPaperResponse>() {
                @Override
                public void onSuccess(Call<WallPaperResponse> call, Response<WallPaperResponse> response) {
                    if (getView() != null) {
                        getView().getWallPaperResult(response);
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

    public interface IWallPagerView extends BaseView{
        /**
         * 获取必应每日一图返回
         * @param response BiYingImgResponse
         */
        void getBiYingResult(Response<BiYingImgBean> response);

        /**
         * 壁纸数据返回
         * @param response WallPaperResponse
         */
        void getWallPaperResult(Response<WallPaperResponse> response);

    }
}
