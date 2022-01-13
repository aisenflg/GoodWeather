package com.example.goodweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.mvplibrary.bean.ResidentCity;

import java.util.List;

public class MainChangeCommonlyCityAdapter extends BaseQuickAdapter<ResidentCity, BaseViewHolder> {

    public MainChangeCommonlyCityAdapter(int layoutResId, @Nullable List<ResidentCity> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ResidentCity item) {
        helper.setText(R.id.tv_city, item.getLocation())
                .addOnClickListener(R.id.tv_city);
    }
}
