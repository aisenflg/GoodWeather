package com.example.goodweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.NewSearchCityResponse;

import java.util.List;

public class SearchCityAdapter extends BaseQuickAdapter<NewSearchCityResponse.LocationBean, BaseViewHolder> {


    public SearchCityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<NewSearchCityResponse.LocationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewSearchCityResponse.LocationBean item) {
        helper.setText(R.id.tv_city_name, item.getName());
        helper.addOnClickListener(R.id.tv_city_name);//绑定点击事件

    }
}
