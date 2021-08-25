package com.example.goodweather.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodweather.R;
import com.example.goodweather.adapter.WeatherHourlyAdapter;
import com.example.goodweather.bean.WeatherBean;
import com.example.goodweather.eventbus.TodayHourlyEvent;
import com.example.mvplibrary.base.BaseFragment;
import com.example.mvplibrary.view.WeatherChartView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TodayFragment extends BaseFragment {
    @BindView(R.id.line_char)
    WeatherChartView lineChar;//折线图
    @BindView(R.id.rv_hourly)
    RecyclerView rvHourly;//逐三小时天气列表

    List<WeatherBean.HeWeather6Bean.HourlyBean> mList = new ArrayList<>();
    WeatherHourlyAdapter mAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TodayHourlyEvent event) {//接收
        List<WeatherBean.HeWeather6Bean.HourlyBean> data = new ArrayList<>();
        data.addAll(event.mHourlyBean);
        Log.i("dayArray", data.get(0).getCond_txt());
        int[] dayArray = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            dayArray[i] = Integer.parseInt(event.mHourlyBean.get(i).getTmp());
        }
        Log.i("dayArray", dayArray.toString());

        lineChar.setTempDay(dayArray);
        lineChar.invalidate();
        //列表
        initList(data);

    }

    private void initList(List<WeatherBean.HeWeather6Bean.HourlyBean> data) {
        mAdapter = new WeatherHourlyAdapter(R.layout.item_weather_hourly_hot_list, mList);
        rvHourly.setLayoutManager(new LinearLayoutManager(context));
        rvHourly.setAdapter(mAdapter);

        mList.clear();
        mList.addAll(data);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}