package com.example.goodweather.adapter;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.utils.DateUtils;
import com.example.goodweather.utils.WeatherUtil;

import java.util.List;

public class HourlyWorldCityAdapter extends BaseQuickAdapter<HourlyResponse.HourlyBean, BaseViewHolder> {

    public HourlyWorldCityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<HourlyResponse.HourlyBean> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void convert(BaseViewHolder helper, HourlyResponse.HourlyBean item) {
        String time = DateUtils.updateTime(item.getFxTime());
        helper.setText(R.id.tv_time, WeatherUtil.showTimeInfo(time) + time)//时间
                .setText(R.id.tv_temperature, item.getTemp() + "℃")
                .setText(R.id.tv_weather_state, item.getText())
                .setText(R.id.tv_wind_info, item.getWindDir() + "，" + item.getWindScale() + "级");//温度

        //天气状态图片
        ImageView weatherStateIcon = helper.getView(R.id.iv_weather_state);
        int code = Integer.parseInt(item.getIcon());//获取天气状态码，根据状态码来显示图标
        WeatherUtil.changeIcon(weatherStateIcon, code);

    }
}
