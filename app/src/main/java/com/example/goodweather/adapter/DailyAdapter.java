package com.example.goodweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.utils.DateUtils;
import com.example.goodweather.utils.WeatherUtil;

import java.util.List;

public class DailyAdapter extends BaseQuickAdapter<DailyResponse.DailyBean, BaseViewHolder> {

    public DailyAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<DailyResponse.DailyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyResponse.DailyBean item) {
        helper.setText(R.id.tv_date, DateUtils.dateSplitPlus(item.getFxDate()) + DateUtils.Week(item.getFxDate()))//日期
                .setText(R.id.tv_temp_height,  item.getTempMax() + "℃")//最高温
                .setText(R.id.tv_temp_low,"/" + item.getTempMin() + "℃" );//最低温

        //天气状态图片
        ImageView weatherStateIcon = helper.getView(R.id.iv_weather_state);
        int code = Integer.parseInt(item.getIconDay());//获取天气状态码，根据状态码来显示图标
        WeatherUtil.changeIcon(weatherStateIcon,code);//调用工具类中写好的方法

        helper.addOnClickListener(R.id.item_forecast);//绑定点击事件的id

    }
}
