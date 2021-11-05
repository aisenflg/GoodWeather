package com.example.goodweather.adapter;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.goodweather.R;
import com.example.goodweather.bean.WarningResponse;
import com.example.goodweather.utils.DateUtils;
import com.example.goodweather.utils.WeatherUtil;

import java.util.List;

public class WarnAdapter extends BaseQuickAdapter<WarningResponse.WarningBean, BaseViewHolder> {
    public WarnAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<WarningResponse.WarningBean> data) {
        super(layoutResId, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void convert(BaseViewHolder helper, WarningResponse.WarningBean item) {
        TextView tvTime = helper.getView(R.id.tv_time);
        String time = DateUtils.updateTime(item.getPubTime());
        tvTime.setText("预警发布时间：" + WeatherUtil.showTimeInfo(time) + time);

        helper.setText(R.id.tv_city, item.getSender())//地区
                .setText(R.id.tv_type_name_and_level,
                        item.getTypeName() + item.getLevel() + "预警")//预警类型名称和等级
                .setText(R.id.tv_content, item.getText());//预警详情内容

    }
}
