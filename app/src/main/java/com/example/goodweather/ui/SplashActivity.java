package com.example.goodweather.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.goodweather.R;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.contract.SplashContrat;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.SPUtils;
import com.example.goodweather.utils.ToastUtils;
import com.example.mvplibrary.bean.Country;
import com.example.mvplibrary.mvp.MvpActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class SplashActivity extends MvpActivity<SplashContrat.SplashPresenter> implements SplashContrat.ISplashView {

    private List<Country> list;//数据列表
    private RxPermissions rxPermissions;//权限请求框架

    @Override
    public void initData(Bundle savedInstanceState) {
        initCountryData();
        rxPermissions = new RxPermissions(this);//实例化这个权限请求框架，否则会报错
        permissionVersion();//权限判断


    }

    //权限判断
    private void permissionVersion() {
        if (Build.VERSION.SDK_INT >= 23) {//6.0或6.0以上
            //动态权限申请
            permissionsRequest();
        } else {//6.0以下
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            ToastUtils.showShortToast(this, "你的版本在Android6.0以下，不需要动态申请权限。");
        }
    }

    //动态权限申请
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//申请成功
                            //得到权限可以进入APP
                            //加载世界国家数据到本地数据库,已有则不加载
                            initCountryData();
                        } else {//申请失败
                            SplashActivity.this.finish();
                            ToastUtils.showShortToast(SplashActivity.this, "权限未开启");
                        }
                    }
                });
    }




    private void initCountryData() {
        list = LitePal.findAll(Country.class);
        if (list.size() > 0) {
            goToMain();
        } else {
            //第一次加载
            InputStreamReader is = null;
            try {
                is = new InputStreamReader(getAssets().open("world_country.csv"), "UTF-8");
                BufferedReader bf = new BufferedReader(is);
                bf.readLine();
                String line;
                while ((line = bf.readLine()) != null) {
                    String[] result = line.split(",");
                    Country country = new Country();
                    country.setName(result[0]);
                    country.setCode(result[1]);
                    country.save();
                }
                goToMain();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void goToMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }, 1000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    /**
     * 必应壁纸数据返回
     *
     * @param response BiYingImgResponse
     */
    @Override
    public void getBiYingResult(Response<BiYingImgBean> response) {
        if (response.body().getImages() != null) {
            //得到的图片地址是没有前缀的，所以加上前缀否则显示不出来
            String biyingUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            SPUtils.putString(Constant.EVERYDAY_TIP_IMG,biyingUrl,context);
        } else {
            ToastUtils.showShortToast(context, "未获取到必应的图片");
        }
    }


    @Override
    public void getDataFailed() {

    }

    @Override
    protected SplashContrat.SplashPresenter createPresent() {
        return new SplashContrat.SplashPresenter();
    }
}