package com.example.goodweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.WorldCityResponse;

import java.util.List;

public class WorldCityAdapter extends BaseQuickAdapter<WorldCityResponse.TopCityListBean, BaseViewHolder> {
    public WorldCityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<WorldCityResponse.TopCityListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorldCityResponse.TopCityListBean item) {
        helper.setText(R.id.tv_city,item.getName());
        helper.addOnClickListener(R.id.tv_city);
    }
}
