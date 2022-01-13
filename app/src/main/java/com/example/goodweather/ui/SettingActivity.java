package com.example.goodweather.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.goodweather.R;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.SPUtils;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.mvplibrary.base.BaseActivity;
import com.example.mvplibrary.view.SwitchButton;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wb_everyday)
    SwitchButton wbEveryday;

    @Override
    public void initData(Bundle savedInstanceState) {
        //白色状态栏
        StatusBarUtil.setStatusBarColor(context, R.color.white);
        //黑色字体
        StatusBarUtil.StatusBarLightMode(context);
        Back(toolbar);

        boolean isChecked = SPUtils.getBoolean(Constant.EVERYDAY_POP_BOOLEAN, true, context);

        wbEveryday.setChecked(isChecked);

        wbEveryday.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SPUtils.putBoolean(Constant.EVERYDAY_POP_BOOLEAN, true, context);
                } else {
                    SPUtils.putBoolean(Constant.EVERYDAY_POP_BOOLEAN, false, context);
                }
            }
        });
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }
}