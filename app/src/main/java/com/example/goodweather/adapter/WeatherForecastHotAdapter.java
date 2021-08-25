package com.example.goodweather.adapter;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.WeatherBean;
import com.example.goodweather.utils.DateUtils;

import java.util.List;

public class WeatherForecastHotAdapter extends BaseQuickAdapter<WeatherBean.HeWeather6Bean.DailyForecastBean, BaseViewHolder> {
    public WeatherForecastHotAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<WeatherBean.HeWeather6Bean.DailyForecastBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeatherBean.HeWeather6Bean.DailyForecastBean item) {
        helper.setText(R.id.tv_date, item.getDate())//日期
                .setText(R.id.tv_day, DateUtils.Week(item.getDate()))
                .setText(R.id.tv_wind_dir_and_sc, item.getWind_dir() + item.getWind_sc() + "级")
                .setText(R.id.tv_uv_index, uvIndex(Integer.parseInt(item.getUv_index())))//紫外线强度
                .setText(R.id.tv_hum, item.getHum() + "%")//相对湿度
                .setText(R.id.tv_pres, item.getPres() + "hPa")//大气压强
        ;
        //最低温和最高温
        TextView minAndMax = helper.getView(R.id.tv_tem_max_min);
        minAndMax.setText(item.getTmp_min() + "/" + item.getTmp_max());
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
        minAndMax.setTypeface(typeface);

        helper.addOnClickListener(R.id.item_forecast);//绑定点击事件的id

    }

    private String uvIndex(int level) {
        String result = null;
        if (level <= 2) {
            result = "最弱";
        } else if (level > 2 && level < 5) {
            result = "弱";
        } else if (level > 4 && level < 7) {
            result = "中等";
        } else if (level > 6 && level < 10) {
            result = "强";
        } else {
            result = "很强";
        }
        return result;
    }


}
