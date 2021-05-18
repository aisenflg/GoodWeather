package com.example.goodweather.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.CityBean;

import java.util.List;

/**
 * 区县适配器列表
 */
public class AreaAdapter extends BaseQuickAdapter<CityBean.Citydata.AreaBean, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CityBean.Citydata.AreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBean.Citydata.AreaBean item) {
        helper.setText(R.id.tv_city,item.getName());//区/县的名称
        helper.addOnClickListener(R.id.item_city);//点击事件 点击之后得到区/县  然后查询天气数据
    }
}
