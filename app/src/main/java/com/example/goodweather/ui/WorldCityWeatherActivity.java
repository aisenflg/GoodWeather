package com.example.goodweather.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodweather.R;
import com.example.goodweather.adapter.HourlyWorldCityAdapter;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.goodweather.contract.WorldCityWeatherContract;
import com.example.goodweather.utils.CodeToStringUtils;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.goodweather.utils.WeatherUtil;
import com.example.mvplibrary.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WorldCityWeatherActivity extends MvpActivity<WorldCityWeatherContract.WorldCityWeatherPresenter> implements WorldCityWeatherContract.IWorldCityWeatherView {
    @BindView(R.id.tv_title)
    TextView tvTitle;//城市
    @BindView(R.id.toolbar)
    Toolbar toolbar;//标题bar
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;//温度
    @BindView(R.id.iv_weather_state)
    ImageView ivWeatherState;//天气状况图片
    @BindView(R.id.tv_tem_max)
    TextView tvTemMax;//最高温
    @BindView(R.id.tv_tem_min)
    TextView tvTemMin;//最低温
    @BindView(R.id.rv_hourly)
    RecyclerView rvHourly;//逐小时列表
    @BindView(R.id.tv_weather_state)
    TextView tvWeatherState;//天气状态文字描述
    @BindView(R.id.tv_wind_state)
    TextView tvWindState;//风状态文字描述

    HourlyWorldCityAdapter mAdapter;//逐小时列表适配器
    List<HourlyResponse.HourlyBean> mList = new ArrayList<>();//列表数据

    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_world_city_weather;
    }

    @Override
    protected WorldCityWeatherContract.WorldCityWeatherPresenter createPresent() {
        return new WorldCityWeatherContract.WorldCityWeatherPresenter();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        StatusBarUtil.transparencyBar(context);//设置状态栏背景颜色
        Back(toolbar);
        showLoadingDialog();//加载弹窗

        mAdapter = new HourlyWorldCityAdapter(R.layout.item_weather_hourly_world_list, mList);
        rvHourly.setLayoutManager(new LinearLayoutManager(context));
        rvHourly.setAdapter(mAdapter);

        String locationId = getIntent().getStringExtra("locationId");//获取上一个页面传递过来的城市id
        tvTitle.setText(getIntent().getStringExtra("name"));//城市名称显示
        mPresent.nowWeather(locationId);//查询实况天气
        mPresent.dailyWeather(locationId);//查询天气预报
        mPresent.hourlyWeather(locationId);//查询逐小时天气预报
    }


    /**
     * 实况天气返回  V7
     *
     * @param response
     */
    @Override
    public void getNowResult(NowResponse response) {

        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/GenJyuuGothic-ExtraLight.ttf");
            tvTemperature.setText(response.getNow().getTemp() + "°");
            tvTemperature.setTypeface(typeface);//使用字体
            int code = Integer.parseInt(response.getNow().getIcon());//获取天气状态码，根据状态码来显示图标
            WeatherUtil.changeIcon(ivWeatherState, code);//调用工具类中写好的方法

            tvWeatherState.setText("当前：" + response.getNow().getText());
            tvWindState.setText(response.getNow().getWindDir()+"   "+response.getNow().getWindScale()+"级");
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }

    /**
     * 天气预报 V7
     *
     * @param response
     */
    @Override
    public void getDailyResult(DailyResponse response) {
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.getDaily() != null && response.getDaily().size() > 0) {
                tvTemMax.setText(response.getDaily().get(0).getTempMax());
                tvTemMin.setText(" / " + response.getDaily().get(0).getTempMin());
            } else {
                ToastUtils.showShortToast(context, "暂无天气预报数据");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }

    /**
     * 逐小时天气预报 V7
     *
     * @param response
     */
    @Override
    public void getHourlyResult(HourlyResponse response) {

        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            List<HourlyResponse.HourlyBean> data = response.getHourly();
            if (data != null && data.size() > 0) {
                mList.clear();
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
                dismissLoadingDialog();
            } else {
                ToastUtils.showShortToast(context, "逐小时天气查询不到");
            }

        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }

    //异常返回
    @Override
    public void getDataFailed() {
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "请求超时");
    }

}