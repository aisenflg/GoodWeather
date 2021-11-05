package com.example.goodweather.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodweather.R;
import com.example.goodweather.adapter.WarnAdapter;
import com.example.goodweather.bean.WarningResponse;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.mvplibrary.base.BaseActivity;
import com.google.gson.Gson;

import butterknife.BindView;

import static com.example.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimation;

public class WarnActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv)
    RecyclerView rv;

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(context);//透明状态栏
        Back(toolbar);
        WarningResponse response = new Gson().fromJson(getIntent().getStringExtra("warnBodyString"),WarningResponse.class);
        WarnAdapter adapter = new WarnAdapter(R.layout.item_warn_list,response.getWarning());
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        runLayoutAnimation(rv);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_warn;
    }
}