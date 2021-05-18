package com.example.goodweather.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.CityBean;

import java.util.List;

/**
 * 市列表适配器
 */
public class CityAdapter extends BaseQuickAdapter<CityBean.Citydata, BaseViewHolder> {
    public CityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CityBean.Citydata> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBean.Citydata item) {
        helper.setText(R.id.tv_city,item.getName());//市名称
        helper.addOnClickListener(R.id.item_city);//点击事件  点击进入区/县列表
    }

}
