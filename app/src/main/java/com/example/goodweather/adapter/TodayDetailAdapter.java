package com.example.goodweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.TodayDetailBean;

import java.util.List;

public class TodayDetailAdapter extends BaseQuickAdapter<TodayDetailBean, BaseViewHolder> {

    public TodayDetailAdapter(int layoutResId, @Nullable List<TodayDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayDetailBean item) {
        ImageView imageView = helper.getView(R.id.iv_icon);
        imageView.setImageResource(item.getIcon());//图标
        helper.setText(R.id.tv_value,item.getValue())//值
                .setText(R.id.tv_name,item.getName());//名称

    }
}
