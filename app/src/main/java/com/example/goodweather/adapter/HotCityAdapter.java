package com.example.goodweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.HotCityBean;

import java.util.List;

public class HotCityAdapter extends BaseQuickAdapter<HotCityBean.HeWeather6Bean.BasicBean, BaseViewHolder> {


    public HotCityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<HotCityBean.HeWeather6Bean.BasicBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCityBean.HeWeather6Bean.BasicBean item) {

        helper.setText(R.id.tv_hot_city_name,item.getLocation())
                .setText(R.id.tv_cnty_and_area,item.getCnty()+" —— "+item.getAdmin_area());
        helper.addOnClickListener(R.id.item_hot_city);

    }
}
