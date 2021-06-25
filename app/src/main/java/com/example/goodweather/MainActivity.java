package com.example.goodweather;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.goodweather.adapter.AreaAdapter;
import com.example.goodweather.adapter.CityAdapter;
import com.example.goodweather.adapter.ProvinceAdapter;
import com.example.goodweather.adapter.WeatherForecastAdapter;
import com.example.goodweather.adapter.WeatherHourlyAdapter;
import com.example.goodweather.bean.AirNowCityBean;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.CityBean;
import com.example.goodweather.bean.HourlyBean;
import com.example.goodweather.bean.LifeStyleBean;
import com.example.goodweather.bean.TodayBean;
import com.example.goodweather.bean.WeatherForecastBean;
import com.example.goodweather.contract.WeatherContract;
import com.example.goodweather.ui.BackgroundManagerActivity;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.SPUtils;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.goodweather.utils.WeatherUtil;
import com.example.mvplibrary.mvp.MvpActivity;
import com.example.mvplibrary.utils.AnimationUtil;
import com.example.mvplibrary.utils.LiWindow;
import com.example.mvplibrary.utils.ObjectUtils;
import com.example.mvplibrary.view.RoundProgressBar;
import com.example.mvplibrary.view.WhiteWindmills;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

import static com.example.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimationRight;

public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView, View.OnClickListener {

    @BindView(R.id.bg)
    LinearLayout bg;//背景图
    @BindView(R.id.tv_info)
    TextView tvInfo;//天气状况
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;//温度
    @BindView(R.id.tv_low_height)
    TextView tvLowHeight;//最高温和最低温
    @BindView(R.id.tv_city)
    TextView tvCity;//城市
    @BindView(R.id.tv_old_time)
    TextView tvOldTime;//最近更新时间
    @BindView(R.id.rv)
    RecyclerView rv;//天气预报
    @BindView(R.id.tv_comf)
    TextView tvComf;//舒适度
    @BindView(R.id.tv_trav)
    TextView tvTrav;//旅游指数
    @BindView(R.id.tv_sport)
    TextView tvSport;//运动指数
    @BindView(R.id.tv_cw)
    TextView tvCw;//洗车指数
    @BindView(R.id.tv_air)
    TextView tvAir;//空气指数
    @BindView(R.id.tv_drsg)
    TextView tvDrsg;//穿衣指数
    @BindView(R.id.tv_flu)
    TextView tvFlu;//感冒指数
    @BindView(R.id.ww_big)
    WhiteWindmills wwBig;//大风车
    @BindView(R.id.ww_small)
    WhiteWindmills wwSmall;//小风车
    @BindView(R.id.tv_wind_direction)
    TextView tvWindDirection;//风向
    @BindView(R.id.tv_wind_power)
    TextView tvWindPower;//风力
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;//刷新布局
    @BindView(R.id.iv_location)
    ImageView ivLocation;//定位图标
    @BindView(R.id.rv_hourly)
    RecyclerView rvHourly;//天气预报
    @BindView(R.id.rpb_aqi)
    RoundProgressBar rpbAqi;//污染指数圆环
    @BindView(R.id.tv_pm10)
    TextView tvPm10;//PM10
    @BindView(R.id.tv_pm25)
    TextView tvPm25;//PM2.5
    @BindView(R.id.tv_no2)
    TextView tvNo2;//二氧化氮
    @BindView(R.id.tv_so2)
    TextView tvSo2;//二氧化硫
    @BindView(R.id.tv_o3)
    TextView tvO3;//臭氧
    @BindView(R.id.tv_co)
    TextView tvCo;//一氧化碳

    private boolean flag = true;//定位图标显示,只有定位的时候显示为true,切换城市和常用城市不显示

    private RxPermissions rxPermissions;//权限请求框架

    private List<WeatherForecastBean.HeWeather6Bean.DailyForecastBean> mList;//初始化数据源
    private WeatherForecastAdapter mAdapter;//初始化适配器
    //定位器
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private List<String> list;//字符串列表
    private List<CityBean> provinceList;//省列表数据
    private List<CityBean.Citydata> citylist;//市列表数据
    private List<CityBean.Citydata.AreaBean> arealist;//区/县列表数据
    private String district; //区/县 更改为全局变量,方便更换城市后也能下拉刷新
    private String city; //市  国控站点数据,用于请求空气质量
    private List<HourlyBean.HeWeather6Bean.HourlyData> mListHourly;//初始化数据源 -> 逐小时天气预报
    private WeatherHourlyAdapter mAdapterHourly;//初始化适配器 逐小时天气预报
    private ProvinceAdapter provinceAdapter;//省数据适配器
    private CityAdapter cityAdapter;//市数据适配器
    private AreaAdapter areaAdapter;//县/区数据适配器
    private String provinceTitle;//标题
    private LiWindow liWindow;//自定义弹窗
    //右上角的弹窗
    private PopupWindow mPopupWindow;
    private AnimationUtil mAnimationUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;//0.5s
    private static final float START_ALPHA = 0.7f;//开始透明读
    private static final float END_ALPHA = 1f;//结束透明度
    //数据初始化  主线程，onCreate方法可以删除了，把里面的代码移动这个initData下面
    @Override
    public void initData(Bundle savedInstanceState) {
        //因为这个框架里面已经放入了绑定，所以这行代码可以注释掉了。
        //ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(context);//透明状态栏
        initList();
        rxPermissions = new RxPermissions(this);//实例化这个权限请求框架，否则会报错
        permissionVersion();//权限判断
        //禁用上拉刷新
        refresh.setEnableLoadMore(false);
        //初始化弹窗
        mPopupWindow = new PopupWindow(this);
        mAnimationUtil = new AnimationUtil();
        setListener();

    }

    private void setListener() {
        ivAdd.setOnClickListener(this);
    }

    //绑定布局文件
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initList() {
        mList = new ArrayList<>();//声明为ArrayList
        mAdapter = new WeatherForecastAdapter(R.layout.item_weather_forecast_list, mList);//为适配器设置布局和数据源
        LinearLayoutManager manager = new LinearLayoutManager(context);//布局管理,默认是纵向
        rv.setLayoutManager(manager);//为列表配置管理器
        rv.setAdapter(mAdapter);//为列表配置适配器
        //逐小时天气预报
        mListHourly = new ArrayList<>();
        mAdapterHourly = new WeatherHourlyAdapter(R.layout.item_weather_hourly_list,mListHourly);
        LinearLayoutManager managerHourly = new LinearLayoutManager(context);
        managerHourly.setOrientation(RecyclerView.HORIZONTAL);
        rvHourly.setLayoutManager(managerHourly);
        rvHourly.setAdapter(mAdapterHourly);

    }

    //绑定Presenter ，这里不绑定会报错
    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return new WeatherContract.WeatherPresenter();
    }


    //权限判断
    private void permissionVersion() {
        if (Build.VERSION.SDK_INT >= 23) {//6.0或6.0以上
            //动态权限申请
            permissionsRequest();
        } else {//6.0以下
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            ToastUtils.showShortToast(this, "你的版本在Android6.0以下，不需要动态申请权限。");
        }
    }

    //动态权限申请
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        //得到权限之后开始定位
                        startLocation();
                    } else {//申请失败
                        ToastUtils.showShortToast(this, "权限未开启");
                    }
                });
    }

    //开始定位
    private void startLocation() {
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
        option.setNeedNewVersionRgc(true);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);
        //启动定位
        mLocationClient.start();

    }

    /**
     * 更多功能弹窗
     */
    private void showAddWindow(){
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.window_add, null));// 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));// 设置pop透明效果
        mPopupWindow.setAnimationStyle(R.style.pop_add);// 设置pop出入动画
        mPopupWindow.setFocusable(true);// 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setTouchable(true);// 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setOutsideTouchable(true);// 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.showAsDropDown(ivAdd, -100, 0);// 相对于 + 号正下面，同时可以设置偏移量
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//关闭弹窗
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });
        //绑定布局中的控件
        TextView changeCity = mPopupWindow.getContentView().findViewById(R.id.tv_change_city);
        TextView changeBg = mPopupWindow.getContentView().findViewById(R.id.tv_change_bg);
        TextView more = mPopupWindow.getContentView().findViewById(R.id.tv_more);
        changeCity.setOnClickListener(view -> {//切换城市
            showCityWindow();
            mPopupWindow.dismiss();
        });
        changeBg.setOnClickListener(view -> {//切换背景
            SPUtils.putString(Constant.DISTRICT,district,context);
            SPUtils.putString(Constant.CITY,city,context);
            startActivity(new Intent(MainActivity.this, BackgroundManagerActivity.class));
            mPopupWindow.dismiss();
        });
        more.setOnClickListener(view -> {//更多功能
            ToastUtils.showShortToast(context,"如果你有什么好的建议，可以博客留言哦！");
            mPopupWindow.dismiss();
        });

    }


    /**
     * 计算动画时间
     */
    private void toggleBright(){
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        mAnimationUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        mAnimationUtil.addUpdateListener(new AnimationUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        mAnimationUtil.addEndListner(new AnimationUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        mAnimationUtil.startAnimator();

    }

    /**
     * 此方法用于改变透明度,达到"变暗"的效果
     */
    private void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    /**
     * 城市弹窗
     */
    private void showCityWindow() {
        provinceList = new ArrayList<>();
        citylist = new ArrayList<>();
        arealist = new ArrayList<>();
        list = new ArrayList<>();
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        ImageView areaBack = (ImageView) view.findViewById(R.id.iv_back_area);
        ImageView cityBack = (ImageView) view.findViewById(R.id.iv_back_city);
        TextView windowTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        liWindow.showRightPopupWindow(view);
        initCityData(recyclerView, areaBack, cityBack, windowTitle);//加载城市列表数据
    }

    /**
     * 省市县数据渲染
     *
     * @param recyclerView 列表
     * @param areaBack     区县返回
     * @param cityBack     市返回
     * @param windowTitle  窗口标题
     */

    private void initCityData(RecyclerView recyclerView, ImageView areaBack, ImageView cityBack, TextView windowTitle) {
        try {
            InputStream inputStream = getResources().getAssets().open("City.txt");//读取数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }
            final JSONArray Data = new JSONArray(stringBuffer.toString());
            //循环这个文件数组、获取数组中每个省对象的名字
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                CityBean bean = new CityBean();
                bean.setName(provinceName);
                provinceList.add(bean);
            }
            //定义省份显示适配器
            provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
            runLayoutAnimationRight(recyclerView);//动画展示
            provinceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    try {
                        //返回上一级数据
                        cityBack.setVisibility(View.VISIBLE);
                        cityBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setAdapter(provinceAdapter);
                                provinceAdapter.notifyDataSetChanged();
                                cityBack.setVisibility(View.GONE);
                                windowTitle.setText("中国");
                            }
                        });
                        //根据当前位置的省份所在的数组位置、获取城市的数组
                        JSONObject provinceObject = Data.getJSONObject(position);
                        windowTitle.setText(provinceList.get(position).getName());
                        provinceTitle = provinceList.get(position).getName();
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //更新列表数据
                        if (citylist != null) {
                            citylist.clear();
                        }

                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject cityObj = cityArray.getJSONObject(i);
                            String cityName = cityObj.getString("name");
                            CityBean.Citydata data = new CityBean.Citydata();
                            data.setName(cityName);
                            citylist.add(data);
                        }

                        cityAdapter = new CityAdapter(R.layout.item_city_list, citylist);
                        LinearLayoutManager manager1 = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(manager1);
                        recyclerView.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();
                        runLayoutAnimationRight(recyclerView);

                        cityAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                try {
                                    //返回上一级数据
                                    areaBack.setVisibility(View.VISIBLE);
                                    areaBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recyclerView.setAdapter(cityAdapter);
                                            cityAdapter.notifyDataSetChanged();
                                            areaBack.setVisibility(View.GONE);
                                            windowTitle.setText(provinceTitle);
                                            arealist.clear();
                                        }
                                    });
                                    //获取区/县的上级 市,用于请求空气质量数据的API接口
                                    city = citylist.get(position).getName();
                                    //根据当前城市数组位置 获取地区数据
                                    windowTitle.setText(citylist.get(position).getName());
                                    JSONObject cityJsonObj = cityArray.getJSONObject(position);
                                    JSONArray areaJsonArray = cityJsonObj.getJSONArray("area");
                                    if (arealist != null) {
                                        arealist.clear();
                                    }
                                    if (list != null) {
                                        list.clear();
                                    }
                                    for (int i = 0; i < areaJsonArray.length(); i++) {
                                        list.add(areaJsonArray.getString(i));
                                    }
                                    Log.i("list", list.toString());
                                    for (int j = 0; j < list.size(); j++) {
                                        CityBean.Citydata.AreaBean bean = new CityBean.Citydata.AreaBean();
                                        bean.setName(list.get(j).toString());
                                        arealist.add(bean);
                                    }
                                    areaAdapter = new AreaAdapter(R.layout.item_city_list, arealist);
                                    LinearLayoutManager manager2 = new LinearLayoutManager(context);

                                    recyclerView.setLayoutManager(manager2);
                                    recyclerView.setAdapter(areaAdapter);
                                    areaAdapter.notifyDataSetChanged();
                                    runLayoutAnimationRight(recyclerView);

                                    areaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                        @Override
                                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                            showLoadingDialog();
                                            district = arealist.get(position).getName();//选择的城市
                                            mPresent.todayWeather(context, district);//今日天气
                                            mPresent.weatherForecast(context, district);//天气预报
                                            mPresent.lifeStyle(context, district);//生活指数
                                            mPresent.getHourly(context,district); //逐小时天气预报
                                            mPresent.getAirNowCityResult(context,city);  //获取空气质量数据
                                            flag = false;//切换城市不属于定位,因此隐藏图标
                                            liWindow.closePopupWindow();

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                showAddWindow();//更多功能弹窗
                toggleBright();//计算动画时间
                break;
        }
    }


    /**
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取区/县
            district = location.getDistrict();
            //获取市
            city = location.getCity();
            //在数据请求之前加载等待弹窗,结果返回后关闭
            showLoadingDialog();
            //获取今天的天气数据
            mPresent.todayWeather(context, district);
            //获取天气预报数据
            mPresent.weatherForecast(context, district);
            //获取生活指数有
            mPresent.lifeStyle(context, district);
//            //获取必应每日一图
//            mPresent.biying(context);
            //逐小时天气预报
            mPresent.getHourly(context,district);
            //获取空气质量数据
            mPresent.getAirNowCityResult(context,city);

            //下拉刷新
            refresh.setOnRefreshListener(refreshLayout -> {
                //获取今天的天气数据
                mPresent.todayWeather(context, district);
                //获取天气预报数据
                mPresent.weatherForecast(context, district);
                //获取生活指数有
                mPresent.lifeStyle(context, district);
                //逐小时天气预报
                mPresent.getHourly(context,district);
                //获取空气质量数据
                mPresent.getAirNowCityResult(context,city);
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();//在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
        if(district == null){
            //取出缓存
            district = SPUtils.getString(Constant.DISTRICT,"",context);
            city = SPUtils.getString(Constant.CITY,"",context);
        }

        isOpenChangeBg();//是否开启了切换背景

        //获取今天的天气数据
        mPresent.todayWeather(context, district);
        //获取天气预报数据
        mPresent.weatherForecast(context, district);
        //获取生活指数数据
        mPresent.lifeStyle(context, district);
        //获取逐小时天气数据
        mPresent.getHourly(context, district);
        //获取空气质量数据
        mPresent.getAirNowCityResult(context, city);
    }

    //判断是否开启了切换背景，没有开启则用默认的背景
    private void isOpenChangeBg() {
        boolean isEverydayImg = SPUtils.getBoolean(Constant.EVERYDAY_IMG,false,context);//每日图片
        boolean isImgList = SPUtils.getBoolean(Constant.IMG_LIST,false,context);//图片列表
        boolean isCustomImg = SPUtils.getBoolean(Constant.CUSTOM_IMG,false,context);//手动定义
        //因为只有有一个为true，其他两个就都会是false,所以可以一个一个的判断
        if(isEverydayImg != true && isImgList != true && isCustomImg != true){
            //当所有开关都没有打开的时候用默认的图片
            bg.setBackgroundResource(R.mipmap.pic_bg);
        }else {
            if(isEverydayImg!=false){//开启每日一图
                mPresent.biying(context);
            }else if(isImgList!=false){//开启图片列表
                int position = SPUtils.getInt(Constant.IMG_POSITION,-1,context);
                switch (position){
                    case 0:
                        bg.setBackgroundResource(R.drawable.img_1);
                        break;
                    case 1:
                        bg.setBackgroundResource(R.drawable.img_2);
                        break;
                    case 2:
                        bg.setBackgroundResource(R.drawable.img_3);
                        break;
                    case 3:
                        bg.setBackgroundResource(R.drawable.img_4);
                        break;
                    case 4:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 5:
                        bg.setBackgroundResource(R.drawable.img_6);
                        break;
                }
            }else if(isCustomImg != false){
                String imgPath = SPUtils.getString(Constant.CUSTOM_IMG_PATH,"",context);
                Glide.with(context)
                        .asBitmap()
                        .load(imgPath)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                                bg.setBackground(drawable);
                            }

                            @Override
                            public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                                ToastUtils.showShortToast(context, "数据为空");
                            }
                        });
            }
        }
    }


    //查询当天天气，请求成功后的数据返回
    @Override
    public void getTodayWeatherResult(Response<TodayBean> response) {
        refresh.finishRefresh();
        dismissLoadingDialog();//关闭弹窗
        //数据返回后关闭定位
        mLocationClient.stop();
        if (response.body().getHeWeather6().get(0).getBasic() != null) {//得到数据不为空则进行数据显示
            //数据渲染显示出来
            if (flag) {
                ivLocation.setVisibility(View.VISIBLE);//显示定位图标
            } else {
                ivLocation.setVisibility(View.GONE);//隐藏定位图标
            }
            tvTemperature.setText(response.body().getHeWeather6().get(0).getNow().getTmp());//温度
            tvCity.setText(response.body().getHeWeather6().get(0).getBasic().getLocation());//城市
            tvInfo.setText(response.body().getHeWeather6().get(0).getNow().getCond_txt());//天气状况
            //修改上次更新时间
            String dataTime = response.body().getHeWeather6().get(0).getUpdate().getLoc();
            String time = dataTime.substring(11);
            tvOldTime.setText("上次更新时间：" + WeatherUtil.showTimeInfo(time)+time);

            tvWindDirection.setText("风向     " + response.body().getHeWeather6().get(0).getNow().getWind_dir());//风向
            tvWindPower.setText("风力     " + response.body().getHeWeather6().get(0).getNow().getWind_sc() + "级");//风力
            wwBig.startRotate();//大风车开始转动
            wwSmall.startRotate();//小风车开始转动
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    //查询天气预报，请求成功后的数据返回
    @Override
    public void getWeatherForecastResult(Response<WeatherForecastBean> response) {
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            //最低温和最高温
            tvLowHeight.setText(response.body().getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min() + " / " +
                    response.body().getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max() + "℃");

            if (response.body().getHeWeather6().get(0).getDaily_forecast() != null) {
                List<WeatherForecastBean.HeWeather6Bean.DailyForecastBean> data
                        = response.body().getHeWeather6().get(0).getDaily_forecast();
                mList.clear();//添加数据之前先清除
                mList.addAll(data);//添加数据
                mAdapter.notifyDataSetChanged();//刷新列表

            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    @Override
    public void getLifeStyleResult(Response<LifeStyleBean> response) {
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            List<LifeStyleBean.HeWeather6Bean.LifestyleData> data = response.body().getHeWeather6().get(0).getLifestyle();
            if (!ObjectUtils.isEmpty(data)) {
                for (int i = 0; i < data.size(); i++) {
                    if (("comf").equals(data.get(i).getType())) {
                        tvComf.setText("舒适度：" + data.get(i).getTxt());
                    } else if (("drsg").equals(data.get(i).getType())) {
                        tvDrsg.setText("穿衣指数：" + data.get(i).getTxt());
                    } else if (("flu").equals(data.get(i).getType())) {
                        tvFlu.setText("感冒指数：" + data.get(i).getTxt());
                    } else if (("sport").equals(data.get(i).getType())) {
                        tvSport.setText("运动指数：" + data.get(i).getTxt());
                    } else if (("trav").equals(data.get(i).getType())) {
                        tvTrav.setText("旅游指数：" + data.get(i).getTxt());
                    } else if (("cw").equals(data.get(i).getType())) {
                        tvCw.setText("洗车指数：" + data.get(i).getTxt());
                    } else if (("air").equals(data.get(i).getType())) {
                        tvAir.setText("空气指数：" + data.get(i).getTxt());
                    }
                }

            } else {
                ToastUtils.showShortToast(context, "生活指数数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    @Override
    public void getBiYingResult(Response<BiYingImgBean> response) {
        dismissLoadingDialog();
        if (response.body().getImages() != null) {
            //图片地址没有前缀,所以需要加上前缀
            String imgUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            Glide.with(context)
                    .asBitmap()
                    .load(imgUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                            bg.setBackground(drawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                            ToastUtils.showShortToast(context, "数据为空");
                        }
                    });
        }
    }

    @Override
    public void getHourly(Response<HourlyBean> response) {
        dismissLoadingDialog();//关闭弹窗
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            if (response.body().getHeWeather6().get(0).getHourly() != null) {
                List<HourlyBean.HeWeather6Bean.HourlyData> data = response.body().getHeWeather6().get(0).getHourly();
                mListHourly.clear();
                mListHourly.addAll(data);
                mAdapterHourly.notifyDataSetChanged();
            }else {
                ToastUtils.showShortToast(context,"逐小时预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }

    }

    @Override
    public void getAirNowCityResult(Response<AirNowCityBean> response) {
        dismissLoadingDialog();//关闭弹窗
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            AirNowCityBean.HeWeather6Bean.AirNowCitydata data = response.body().getHeWeather6().get(0).getAir_now_city();
            if (!ObjectUtils.isEmpty(data) && data != null) {
                //污染指数
                rpbAqi.setMaxProgress(500);//最大进度，用于计算
                rpbAqi.setMinText("0");//设置显示最小值
                rpbAqi.setMinTextSize(32f);
                rpbAqi.setMaxText("500");//设置显示最大值
                rpbAqi.setMaxTextSize(32f);
                rpbAqi.setProgress(Float.valueOf(data.getAqi()));//当前进度
                rpbAqi.setArcBgColor(getResources().getColor(R.color.arc_bg_color));//圆弧的颜色
                rpbAqi.setProgressColor(getResources().getColor(R.color.arc_progress_color));//进度圆弧的颜色
                rpbAqi.setFirstText(data.getQlty());//空气质量描述  取值范围：优，良，轻度污染，中度污染，重度污染，严重污染
                rpbAqi.setFirstTextSize(44f);
                rpbAqi.setSecondText(data.getAqi());//空气质量值
                rpbAqi.setSecondTextSize(64f);
                rpbAqi.setMinText("0");
                rpbAqi.setMinTextColor(getResources().getColor(R.color.arc_progress_color));

                tvPm10.setText(data.getPm10());//PM10
                tvPm25.setText(data.getPm25());//PM2.5
                tvNo2.setText(data.getNo2());//二氧化氮
                tvSo2.setText(data.getSo2());//二氧化硫
                tvO3.setText(data.getO3());//臭氧
                tvCo.setText(data.getCo());//一氧化碳
            }

        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }


    //数据请求失败返回
    @Override
    public void getDataFailed() {
        refresh.finishRefresh();//关闭刷新
        dismissLoadingDialog();//关闭弹窗
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }


}