package com.example.goodweather.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.goodweather.R;
import com.example.goodweather.bean.WeatherBean;
import com.example.goodweather.contract.HotCityWeatherContract;
import com.example.goodweather.eventbus.ForecastEvent;
import com.example.goodweather.eventbus.TodayHourlyEvent;
import com.example.goodweather.fragment.ForecastFragment;
import com.example.goodweather.fragment.TodayFragment;
import com.example.goodweather.utils.CodeToStringUtils;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.goodweather.utils.WeatherUtil;
import com.example.mvplibrary.mvp.MvpActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import retrofit2.Response;

public class HotCityWeatherActivity extends MvpActivity<HotCityWeatherContract.HotCityWeatherPresenter>
        implements HotCityWeatherContract.IHotCityWeatherView {

    @BindView(R.id.tv_title)
    TextView tvTitle;//城市
    @BindView(R.id.toolbar)
    Toolbar toolbar;//标题bar
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;//温度
    @BindView(R.id.iv_weather_state)
    ImageView ivWeatherState;//天气状况图片
    @BindView(R.id.tv_tem_max)
    TextView tvTemMax;//最高温
    @BindView(R.id.tv_tem_min)
    TextView tvTemMin;//最低温
    @BindView(R.id.tab)
    SmartTabLayout tab;//标签视图
    @BindView(R.id.vp)
    ViewPager vp;


    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_city_weather;
    }

    @Override
    protected HotCityWeatherContract.HotCityWeatherPresenter createPresent() {
        return new HotCityWeatherContract.HotCityWeatherPresenter();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        showLoadingDialog();
        StatusBarUtil.setStatusBarColor(context, R.color.pink);//设置状态栏背景颜色
        Back(toolbar);
        initView();
        String location = getIntent().getStringExtra("location");
        mPresent.hotCityWeather(location, context);

    }

    private void initView() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                        .add("Today", TodayFragment.class)
                        .add("Forecast", ForecastFragment.class)
                        .create());
        vp.setAdapter(adapter);
        tab.setViewPager(vp);
    }


    @Override
    public void getHotCityWeatherResult(Response<WeatherBean> response) {
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            if (response.body().getHeWeather6().get(0).getBasic() != null) {//得到数据不为空则进行数据显示
                //基本天气信息
                WeatherBean.HeWeather6Bean.NowBean nowBean = response.body().getHeWeather6().get(0).getNow();
                tvTitle.setText(response.body().getHeWeather6().get(0).getBasic().getLocation());
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
                tvTemperature.setText(nowBean.getTmp());
                tvTemperature.setTypeface(typeface);//使用字体
                int code = Integer.parseInt(nowBean.getCond_code());//获取天气状态码，根据状态码来显示图标
                WeatherUtil.changeIcon(ivWeatherState, code);//调用工具类中写好的方法
                //最低温和最高温
                tvTemMax.setText(response.body().getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max());
                tvTemMin.setText(" / " + response.body().getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min() + " ℃");

                //传递数据到TodayFragment和ForcastFragment
                EventBus.getDefault().post(new TodayHourlyEvent(response.body().getHeWeather6().get(0).getHourly()));
                EventBus.getDefault().post(new ForecastEvent(response.body().getHeWeather6().get(0).getDaily_forecast()));

                dismissLoadingDialog();
            } else {
                ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getHeWeather6().get(0).getStatus()));
            }
        }
    }

    @Override
    public void getDataFailed() {
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }
}