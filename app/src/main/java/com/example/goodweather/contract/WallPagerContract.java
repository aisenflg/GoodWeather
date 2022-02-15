package com.example.goodweather.contract;

import com.example.goodweather.api.ApiService;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.WallPaperResponse;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.ServiceGenerator;
import com.example.mvplibrary.newnet.NetworkApi;
import com.example.mvplibrary.newnet.observer.BaseObserver;

public class WallPagerContract {
    public static class WallPagerPresenter extends BasePresenter<IWallPagerView>{
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


        /**
         * 获取壁纸数据
         */
        public void getWallPaper() {
            // 6 表示访问网络壁纸接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 6);

            service.getWallPaper()
                    .compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
                        @Override
                        public void onSuccess(WallPaperResponse wallPaperResponse) {
                            if (getView() != null) {
                                getView().getWallPaperResult(wallPaperResponse);
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            if (getView() != null) {
                                getView().getDataFailed();
                            }
                        }
                    }));
//            service.getWallPaper().enqueue(new NetCallBack<WallPaperResponse>() {
//                @Override
//                public void onSuccess(Call<WallPaperResponse> call, Response<WallPaperResponse> response) {
//                    if (getView() != null) {
//                        getView().getWallPaperResult(response);
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

    public interface IWallPagerView extends BaseView{
        /**
         * 获取必应每日一图返回
         * @param response BiYingImgResponse
         */
        void getBiYingResult(BiYingImgBean response);

        /**
         * 壁纸数据返回
         * @param response WallPaperResponse
         */
        void getWallPaperResult(WallPaperResponse response);

    }
}
