package com.example.goodweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.SearchCityBean;

import java.util.List;

public class SearchCityAdapter extends BaseQuickAdapter<SearchCityBean.HeWeather6Bean.BasicBean, BaseViewHolder> {

    public SearchCityAdapter(int layoutResId, @Nullable List<SearchCityBean.HeWeather6Bean.BasicBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, SearchCityBean.HeWeather6Bean.BasicBean item) {
        helper.setText(R.id.tv_city_name, item.getLocation());
        helper.addOnClickListener(R.id.tv_city_name);//绑定点击事件
    }
}
