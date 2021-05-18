package com.example.goodweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.CityBean;

import java.util.List;

/**
 * 省列表适配器
 */
public class ProvinceAdapter extends BaseQuickAdapter<CityBean, BaseViewHolder> {


    public ProvinceAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CityBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityBean item) {
        helper.setText(R.id.tv_city,item.getName());//省名称
        helper.addOnClickListener(R.id.item_city);//点击之后进入市级列表
    }
}
