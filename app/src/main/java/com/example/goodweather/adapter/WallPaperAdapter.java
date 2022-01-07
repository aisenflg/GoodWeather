package com.example.goodweather.adapter;

import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.WallPaperResponse;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class WallPaperAdapter extends BaseQuickAdapter<WallPaperResponse.ResBean.VerticalBean, BaseViewHolder> {

    //定义一个item的高度列表
    List<Integer> mHeightList;
    /**
     * 头部广告
     */
    private String Top = "top";
    /**
     * 底部广告
     */
    private String Bottom = "bottom";


    public WallPaperAdapter(int layoutResId, @Nullable List<WallPaperResponse.ResBean.VerticalBean> data,List<Integer> heightList) {
        super(layoutResId, data);
        this.mHeightList = heightList;
    }

    @Override
    protected void convert(BaseViewHolder helper, WallPaperResponse.ResBean.VerticalBean item) {
        ShapeableImageView imageView = helper.getView(R.id.iv_wallpaper);

        //获取imageView的LayoutParams
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = dip2px(mHeightList.get(helper.getAdapterPosition()));
        //重新设置imageView的高度
        imageView.setLayoutParams(layoutParams);

        if (Top.equals(item.getDesc()) || Bottom.equals(item.getDesc())) {
            imageView.setImageResource(R.mipmap.icon_logo);
        } else {
            Glide.with(mContext).load(item.getImg()).into(imageView);
        }

        helper.addOnClickListener(R.id.item_wallpaper);

    }

    // dp 转成 px
    private int dip2px(float dpVale) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

}
