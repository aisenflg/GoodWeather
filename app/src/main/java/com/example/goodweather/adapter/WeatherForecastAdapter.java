package com.example.goodweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.WeatherBean;
import com.example.goodweather.utils.WeatherUtil;

import java.util.List;

public class WeatherForecastAdapter extends BaseQuickAdapter<WeatherBean.HeWeather6Bean.DailyForecastBean, BaseViewHolder> {

    public WeatherForecastAdapter(int layoutResId, @Nullable List<WeatherBean.HeWeather6Bean.DailyForecastBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, WeatherBean.HeWeather6Bean.DailyForecastBean item) {
        helper.setText(R.id.tv_date, item.getDate())//日期
//                .setText(R.id.tv_info, item.getCond_txt_d())//天气
                .setText(R.id.tv_low_and_height, item.getTmp_min() + "/" + item.getTmp_max() + "℃");//最低温和最高温

        //天气状态图片
        ImageView weatherStateIcon = helper.getView(R.id.iv_weather_state);
        int code = Integer.parseInt(item.getCond_code_d());//获取天气状态码，根据状态码来显示图标
        WeatherUtil.changeIcon(weatherStateIcon,code);//调用工具类中写好的方法
        helper.addOnClickListener(R.id.item_forecast);


    }
}
