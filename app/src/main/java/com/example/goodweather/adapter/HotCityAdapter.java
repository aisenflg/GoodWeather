package com.example.goodweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.HotCityBean;

import java.util.List;

public class HotCityAdapter extends BaseQuickAdapter<HotCityBean.HeWeather6Bean.BasicBean, BaseViewHolder> {
    private int mType;

    public HotCityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<HotCityBean.HeWeather6Bean.BasicBean> data,int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotCityBean.HeWeather6Bean.BasicBean item) {
        ImageView ivMark = helper.getView(R.id.iv_mark);
        ImageView ivOpen = helper.getView(R.id.iv_open);

        if (mType == 0) {//国内
            ivMark.setBackground(mContext.getResources().getDrawable(R.drawable.shape_blue_8));//背景
            ivMark.setImageDrawable(mContext.getDrawable(R.mipmap.icon_hot_city_china));//图标
            ivOpen.setImageDrawable(mContext.getDrawable(R.mipmap.icon_open_blue));//图标

        } else {//国外
            ivMark.setBackground(mContext.getResources().getDrawable(R.drawable.shape_orange_8));//背景
            ivMark.setImageDrawable(mContext.getDrawable(R.mipmap.icon_city));//图标
            ivOpen.setImageDrawable(mContext.getDrawable(R.mipmap.icon_open));//图标
        }

        helper.setText(R.id.tv_hot_city_name,item.getLocation())
                .setText(R.id.tv_cnty_and_area,item.getCnty()+" —— "+item.getAdmin_area());
        helper.addOnClickListener(R.id.item_hot_city);

    }
}
