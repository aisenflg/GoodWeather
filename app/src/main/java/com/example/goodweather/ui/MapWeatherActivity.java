package com.example.goodweather.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.goodweather.R;
import com.example.goodweather.adapter.SevenDailyAdapter;
import com.example.goodweather.adapter.TodayDetailAdapter;
import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.goodweather.bean.SunMoonResponse;
import com.example.goodweather.bean.TodayDetailBean;
import com.example.goodweather.contract.MapWeatherContract;
import com.example.goodweather.utils.CodeToStringUtils;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.DateUtils;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.goodweather.utils.WeatherUtil;
import com.example.goodweather.view.horizonview.HourlyForecastView;
import com.example.goodweather.view.horizonview.IndexHorizontalScrollView;
import com.example.goodweather.view.skyview.SunView;
import com.example.mvplibrary.mvp.MvpActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.goodweather.utils.DateUtils.getCurrentTime;
import static com.example.goodweather.utils.DateUtils.getNowDateNoLimiter;

public class MapWeatherActivity extends MvpActivity<MapWeatherContract.MapWeatherPresenter> implements MapWeatherContract.IMapWeatherView, View.OnClickListener {

    @BindView(R.id.map_view)
    MapView mapView;//地图控件
    @BindView(R.id.tv_city)
    TextView tvCity;//城市
    @BindView(R.id.iv_weather)
    ImageView ivWeather;//天气状态图片描述
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;//温度
    @BindView(R.id.tv_weather_state_tv)
    TextView tvWeatherStateTv;//天气状态文字描述
    @BindView(R.id.tv_wind_info)
    TextView tvWindInfo;//风力风向
    @BindView(R.id.rv_today_detail)
    RecyclerView rvTodayDetail;//今日详情数据列表
    @BindView(R.id.rv_seven_day_daily)
    RecyclerView rvSevenDayDaily;//七天天气预报列表
    @BindView(R.id.tv_more_daily)
    TextView tvMoreDaily;//15日天气预报数据
    @BindView(R.id.tv_uvIndex)
    TextView tvUvIndex;//紫外线强度
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;//湿度
    @BindView(R.id.tv_pressure)
    TextView tvPressure;//大气压
    @BindView(R.id.tv_today_info)
    TextView tvTodayInfo;//今日天气简要描述
    @BindView(R.id.tv_air)
    TextView tvAir;//空气质量
    @BindView(R.id.bottom_sheet_ray)
    RelativeLayout bottomSheetRay;//底部拖动布局
    @BindView(R.id.btn_auto_location)
    FloatingActionButton btnAutoLocation;//重新定位按钮

    @BindView(R.id.tv_line_max_tmp)
    TextView tvLineMaxTmp;//今日最高温
    @BindView(R.id.tv_line_min_tmp)
    TextView tvLineMinTmp;//今日最低温
    @BindView(R.id.hourly)
    HourlyForecastView hourly;//和风自定义逐小时天气渲染控件
    @BindView(R.id.hsv)
    IndexHorizontalScrollView hsv;//和风自定义滚动条

    @BindView(R.id.sun_view)
    SunView sunView;//太阳
    @BindView(R.id.moon_view)
    SunView moonView;//月亮
    @BindView(R.id.tv_moon_state)
    TextView tvMoonState;//月亮状态

    @BindView(R.id.iv_search)
    ImageView ivSearch;//搜索图标
    @BindView(R.id.ed_search)
    EditText edSearch;//搜索输入框
    @BindView(R.id.iv_close)
    ImageView ivClose;//关闭图标
    @BindView(R.id.lay_search)
    RelativeLayout laySearch;//搜索布局


    private String locationId;//城市id
    private List<DailyResponse.DailyBean> dailyList = new ArrayList<>();//七天天气预报
    List<TodayDetailBean> todayDetailList = new ArrayList<>();//当前天气详情

    private SevenDailyAdapter mSevenDailyAdapter;//七天天气预报适配器

    private String dayInfo;//今天白天天气描述
    private String nightInfo;//今天晚上天气描述

    private BottomSheetBehavior bottomSheetBehavior;//底部控件


    private LocationClient mLocationClient;//定位
    private BaiduMap mBaiduMap;//百度地图
    private Marker marker;//标点也可以说是覆盖物
    private BitmapDescriptor bitmap;//标点的图标
    private double markerLatitude = 0;//标点纬度
    private double markerLongitude = 0;//标点经度
    private double latitude;//定位纬度
    private double longitude;//定位经度
    private GeoCoder geoCoder;//百度地址解析
    //定位标识符  因onReceiveLocation会多次执行 执行一次后变为ture;
    private boolean location = false;
    private AutoTransition autoTransition;//过渡动画
    private Animation bigShowAnim;//放大显示
    private Animation smallHideAnim;//缩小隐藏
    private int width;//屏幕宽度
    private boolean isOpen = false;//顶部搜索布局的状态


    /**
     * 定位SDK监听,
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        /**
         * 监听返回数据 MapView 销毁后不在处理新接收的位置
         *
         * @param bdLocation 定位信息
         */
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mapView == null) {//做null处理，避免APP崩溃
                return;
            }
            if (!location) {
                return;
            }
            if (markerLatitude == 0) {
                //自动定位
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                btnAutoLocation.setVisibility(View.GONE);
            } else {
                //标点定位
                latitude = markerLatitude;
                longitude = markerLongitude;
                btnAutoLocation.setVisibility(View.VISIBLE);
            }
            MyLocationData locationData = new MyLocationData.Builder()//定位构造器
                    .accuracy(bdLocation.getRadius())//设置定位数据的精度信息，单位：米
                    .direction(bdLocation.getDirection())//设置定位数据的方向信息
                    .latitude(latitude)//设置定位数据的纬度
                    .longitude(longitude)//设置定位数据的经度
                    .build();//构建生成定位数据对象
            mBaiduMap.setMyLocationData(locationData);//设置定位数据，只有开启定位图层之后才会生效

            //创建一个经纬度构造对象，传入定位返回的经纬度，Latitude是纬度，Longitude是经度,一对经纬度值代表地球上一个地点。
            LatLng latLng = new LatLng(latitude, longitude);
            //根据经纬度进行反编码
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).pageNum(0).pageSize(100));
            MapStatus.Builder builder = new MapStatus.Builder()//创建地图状态构造器
                    .target(latLng)//设置地图中心点，传入经纬度对象
                    .zoom(13.0f);//设置地图缩放级别 13 表示  比例尺/2000米 2公里

            //改变地图状态，使用地图状态更新工厂中的新地图状态方法，传入状态构造器
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            location = false;

        }
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        location = true;

        mBaiduMap.setMyLocationEnabled(true);//开启定位图层

        mLocationClient = new LocationClient(context);//定位初始化

        MyLocationListener listener = new MyLocationListener();//创建定位监听器
        mLocationClient.registerLocationListener(listener);//注册定位监听，否则监听无效
        LocationClientOption option = new LocationClientOption();//创建定位设置
        option.setOpenGps(true);//打开GPS
        option.setCoorType("bd09ll");//设置坐标类型  可以设置BD09LL和GCJ02两种坐标
        option.setScanSpan(0);//设置扫描间隔，单位是毫秒，0  则表示只定位一次，设置毫秒不能低于1000，也就是1秒


        mLocationClient.setLocOption(option);//传入定位设置
        mLocationClient.start();//开始定位

    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initView();
        initLocation();
        initMapOnClick();
    }

    public void initView() {
        StatusBarUtil.transparencyBar(context);//透明状态栏
        StatusBarUtil.StatusBarLightMode(context);//状态栏黑色字体
        //获取屏幕宽高
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //获取屏幕的宽度 像素

        mBaiduMap = mapView.getMap();//赋值对象
        mapView.removeViewAt(1);// 删除百度地图logo
        geoCoder = GeoCoder.newInstance();//赋值
        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);//反编码结果监听
        btnAutoLocation.setOnClickListener(this);
        tvMoreDaily.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        //放大
        bigShowAnim = AnimationUtils.loadAnimation(context, R.anim.scale_big_expand);
        //缩小
        smallHideAnim = AnimationUtils.loadAnimation(context, R.anim.scale_small_close);


        /*获取behavior 控制bottomsheet的 显示 与隐藏*/
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRay);
        /*bottomSheet 的 状态改变 根据不同的状态 做不同的事情*/
        //setBottomSheetCallback 已经过时了 现在使用addBottomSheetCallback

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED://收缩
                        //手动定位时,收缩就要显示浮动按钮,因为这时候你可以操作地图了
                        if (markerLatitude != 0) {//自动定位
                            btnAutoLocation.show();//显示自动定位按钮
                        }
                        scaleAnimation(laySearch,"show");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED://展开
                        //手动定位时,展开就要隐藏浮动按钮,因为这时候你是没有办法操作地图的
                        if (markerLatitude != 0) {//自动定位
                            btnAutoLocation.hide();//隐藏自动定位按钮
                        }
                        if (isOpen) {//顶部搜索为展开状态时
                            //先搜索
                            initClose();
                            //再起一个线程 500毫秒后隐藏这个按钮
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scaleAnimation(laySearch,"hide");
                                }
                            },500);

                        }else {
                            //直接隐藏
                            scaleAnimation(laySearch,"hide");
                        }

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                /*slideOffset bottomSheet 的 移动距离*/
            }
        });

        /**
         * 输入法键盘的搜索监听
         */
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String city = edSearch.getText().toString();
                    if (!TextUtils.isEmpty(city)) {
                        //得到输入的内容
                        geoCoder.geocode(new GeoCodeOption()
                                .city(city)//城市名称
                                .address(city));//详细地址
                    } else {
                        ToastUtils.showShortToast(context, "请输入城市名称");
                    }
                }
                return false;
            }
        });


    }

    /**
     * 初始化地图点击
     */
    private void initMapOnClick() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            //地图内 Poi 单击事件回调函数  那么poi是什么呢？你可以当做兴趣点，
            // 比如我想知道我当前所在地有那些餐厅，那么餐厅就是poi，
            // 而你点击这个poi就会拿到详情的信息数据，当然不在我的业务需求之内，所以只做解释
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }

            //地图单击事件回调函数
            @Override
            public void onMapClick(LatLng latLng) {
                resetLocation(latLng);
            }

        });
    }


    /**
     * 编码结果监听
     */
    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        /**
         * 编码结果返回  就是通过具体位置信息获取坐标
         * @param geoCodeResult  编码返回结果
         */
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Log.d("address-->", "没有检测结果");
                ToastUtils.showShortToast(context, "请输入区/县、市的名称");
                edSearch.setText("");
                return;
            }
            LatLng latLng = geoCodeResult.getLocation();//获得坐标
            Log.d("latLng-->", geoCodeResult.getLevel());
            Log.d("latLng-->", "纬度-->" + latLng.latitude + "经度-->" + latLng.longitude);
            resetLocation(latLng);
        }

        /**
         * 反编码结果返回  就是通过坐标获取具体位置信息
         * @param reverseGeoCodeResult 反编码返回结果
         */
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null
                    || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检测到结果
                return;
            }
            ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
            //需要的地址信息就在AddressComponent 里
            ToastUtils.showShortToast(context, "地址信息：" + addressDetail.province + addressDetail.city + addressDetail.district + addressDetail.street);
            Log.d("dataStr", addressDetail.province + addressDetail.city + addressDetail.district + addressDetail.street);
            mPresent.searchCity(addressDetail.district); //搜索城市ID

        }
    };


    /**
     * 搜索城市
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(NewSearchCityResponse response) {
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.getLocation() != null && response.getLocation().size() > 0) {
                tvCity.setText(response.getLocation().get(0).getName());//城市
                locationId = response.getLocation().get(0).getId();//城市Id
                showLoadingDialog();
                mPresent.nowWeather(locationId);//查询实况天气
                mPresent.airNowWeather(locationId);//空气质量
                mPresent.weatherHourly(locationId);//24小时天气预报
                mPresent.dailyWeather(locationId);//查询天气预报
                mPresent.getSunMoon(locationId, getNowDateNoLimiter());//查询太阳和月亮
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            tvCity.setText("查询城市失败");
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }

    }

    /**
     * 实况天气返回
     *
     * @param response
     */
    @Override
    public void getNowResult(NowResponse response) {
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {//200则成功返回数据
            NowResponse data = response;
            if (data != null) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
                tvTemperature.setText(response.getNow().getTemp() + "°");//温度
                tvTemperature.setTypeface(typeface);//使用字体
                tvWeatherStateTv.setText(data.getNow().getText());//天气状态文字描述
                WeatherUtil.changeIcon(ivWeather, Integer.parseInt(data.getNow().getIcon()));
                tvWindInfo.setText(data.getNow().getWindDir() + data.getNow().getWindScale() + "级");

                tvHumidity.setText(data.getNow().getHumidity() + "%");//湿度
                tvPressure.setText(data.getNow().getPressure() + "hPa");//大气压

                //今日详情
                todayDetailList.clear();
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_temp, data.getNow().getFeelsLike() + "°", "体感温度"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_precip, data.getNow().getPrecip() + "mm", "降水量"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_humidity, data.getNow().getHumidity() + "%", "湿度"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_pressure, data.getNow().getPressure() + "hPa", "大气压强"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_vis, data.getNow().getVis() + "KM", "能见度"));
                todayDetailList.add(new TodayDetailBean(R.mipmap.icon_today_cloud, data.getNow().getCloud() + "%", "云量"));

                TodayDetailAdapter adapter = new TodayDetailAdapter(R.layout.item_today_detail, todayDetailList);
                rvTodayDetail.setLayoutManager(new GridLayoutManager(context, 3));
                rvTodayDetail.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                ToastUtils.showShortToast(context, "暂无实况天气数据");
            }
        } else {//其他状态返回提示文字
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }


    /**
     * 未来七天天气预报数据返回
     *
     * @param response
     */
    @Override
    public void getDailyResult(DailyResponse response) {
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyBean> data = response.getDaily();
            if (data != null && data.size() > 0) {//判空处理

                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getFxDate().equals(DateUtils.getNowDate())) {//今天
                        dayInfo = data.get(i).getTextDay();
                        nightInfo = data.get(i).getTextNight();
                        tvUvIndex.setText(WeatherUtil.uvIndexInfo(data.get(i).getUvIndex()));//紫外线强度
                    }
                }
                dailyList.clear();//添加数据之前先清除
                dailyList.addAll(data);//添加数据
                mSevenDailyAdapter = new SevenDailyAdapter(R.layout.item_seven_day_daily_list, dailyList);
                rvSevenDayDaily.setLayoutManager(new LinearLayoutManager(context));
                rvSevenDayDaily.setAdapter(mSevenDailyAdapter);
                mSevenDailyAdapter.notifyDataSetChanged();//刷新列表

                mPresent.airNowWeather(locationId);//空气质量
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {//异常状态码返回
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }


    /**
     * 空气质量数据返回
     *
     * @param response
     */
    @Override
    public void getAirNowResult(AirNowResponse response) {
        dismissLoadingDialog();
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            AirNowResponse.NowBean data = response.getNow();
            if (response.getNow() != null) {
                tvAir.setText("AQI " + data.getCategory());
                tvTodayInfo.setText("今天，白天" + dayInfo + "，晚上" + nightInfo +
                        "，现在" + tvTemperature.getText().toString() + "，" +
                        WeatherUtil.apiToTip(data.getCategory()) + WeatherUtil.uvIndexToTip(tvUvIndex.getText().toString()));

            } else {
                ToastUtils.showShortToast(context, "空气质量数据为空");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void getSunMoonResult(SunMoonResponse response) {
        dismissLoadingDialog();
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {

            SunMoonResponse data = response;
            if (data != null) {
                String sunRise = getCurrentTime(data.getSunrise());
                String moonRise = getCurrentTime(data.getMoonrise());
                String sunSet = getCurrentTime(data.getSunset());
                String moonSet = getCurrentTime(data.getMoonset());
                String currentTime = getCurrentTime(null);

                sunView.setTimes(sunRise, sunSet, currentTime);
                moonView.setTimes(moonRise, moonSet, currentTime);
                if (data.getMoonPhase() != null && data.getMoonPhase().size() > 0) {
                    tvMoonState.setText(data.getMoonPhase().get(0).getName());
                }

            } else {
                ToastUtils.showShortToast(context, "日出日落数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }

    }

    /**
     * 24小时天气预报数据返回
     *
     * @param response
     */
    @Override
    public void getWeatherHourlyResult(HourlyResponse response) {
        if (response.getCode().equals(Constant.SUCCESS_CODE)) {
            List<HourlyResponse.HourlyBean> hourlyWeatherList = response.getHourly();
            List<HourlyResponse.HourlyBean> data = new ArrayList<>();
            if (hourlyWeatherList.size() > 23) {
                for (int i = 0; i < 24; i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getIcon();
                    String time = data.get(i).getFxTime();
                    time = time.substring(time.length() - 11, time.length() - 9);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setIcon(condCode + "d");
                    } else {
                        data.get(i).setIcon(condCode + "n");
                    }
                }
            } else {
                for (int i = 0; i < hourlyWeatherList.size(); i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getIcon();
                    String time = data.get(i).getFxTime();
                    time = time.substring(time.length() - 11, time.length() - 9);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setIcon(condCode + "d");
                    } else {
                        data.get(i).setIcon(condCode + "n");
                    }
                }
            }

            int minTmp = Integer.parseInt(data.get(0).getTemp());
            int maxTmp = minTmp;
            for (int i = 0; i < data.size(); i++) {
                int tmp = Integer.parseInt(data.get(i).getTemp());
                minTmp = Math.min(tmp, minTmp);
                maxTmp = Math.max(tmp, maxTmp);
            }
            //设置当天的最高最低温度
            hourly.setHighestTemp(maxTmp);
            hourly.setLowestTemp(minTmp);
            if (maxTmp == minTmp) {
                hourly.setLowestTemp(minTmp - 1);
            }
            hourly.initData(data);
            tvLineMaxTmp.setText(maxTmp + "°");
            tvLineMinTmp.setText(minTmp + "°");
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.getCode()));
        }
    }


    /**
     * 错误返回
     */
    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "连接超时，稍后尝试。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto_location: //重新定位
                markerLatitude = 0;
                markerLongitude = 0;
                marker.remove();//清除标点
                initLocation();
                break;

            case R.id.tv_more_daily: //15日天气预报
                Intent intent = new Intent(context, MoreDailyActivity.class);
                intent.putExtra("locationId", locationId);
                intent.putExtra("cityName", tvCity.getText().toString());
                startActivity(intent);
                break;

            case R.id.iv_search://展开
                initExpand();
                break;

            case R.id.iv_close://收缩
                initClose();
                break;
        }
    }

    /**
     * 重新定位
     *
     * @param latLng 坐标
     */
    private void resetLocation(LatLng latLng) {
        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka);// 设置marker图标
        //通过LatLng获取经纬度
        markerLatitude = latLng.latitude;//获取纬度
        markerLongitude = latLng.longitude;//获取经度
        mBaiduMap.clear();//清除之前的图层

        MarkerOptions options = new MarkerOptions()//创建标点marker设置对象
                .position(latLng)//设置标点的定位
                .icon(bitmap);//设置标点图标

        marker = (Marker) mBaiduMap.addOverlay(options);//在地图上显示标点
        //重新定位
        initLocation();
    }



    // dp 转成 px
    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    // px 转成 dp
    private int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    //过渡动画
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void beginDelayedTransition(ViewGroup view) {
        autoTransition = new AutoTransition();
        autoTransition.setDuration(500);
        TransitionManager.beginDelayedTransition(view, autoTransition);
    }


    /**
     * 展开
     */
    public void initExpand() {
        isOpen = true;
        edSearch.setVisibility(View.VISIBLE);//显示输入框
        ivClose.setVisibility(View.VISIBLE);//显示关闭按钮
        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) laySearch.getLayoutParams();
        LayoutParams.width = dip2px(px2dip(width) - 24);//设置展开的宽度
        LayoutParams.setMargins(dip2px(0), dip2px(0), dip2px(0), dip2px(0));
        laySearch.setPadding(14, 0, 14, 0);
        laySearch.setLayoutParams(LayoutParams);

        //开始动画
        beginDelayedTransition(laySearch);

        if (markerLatitude != 0) {//手动定位时
            btnAutoLocation.hide();//隐藏自动定位按钮
        }

    }

    /**
     * 收缩
     */
    private void initClose() {
        isOpen = false;
        edSearch.setVisibility(View.GONE);
        edSearch.setText("");
        ivClose.setVisibility(View.GONE);

        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) laySearch.getLayoutParams();
        LayoutParams.width = dip2px(48);
        LayoutParams.height = dip2px(48);
        LayoutParams.setMargins(dip2px(0), dip2px(0), dip2px(0), dip2px(0));
        laySearch.setLayoutParams(LayoutParams);

        //隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);

        //开始动画
        beginDelayedTransition(laySearch);

        if (markerLatitude != 0) {//自动定位
            btnAutoLocation.show();//隐藏自动定位按钮
        }
    }


    /**
     * 缩放动画
     * @param view 需要缩放的控件
     * @param state 状态  显示或者隐藏
     */
    private void scaleAnimation(View view,String state) {

        switch (state){
            case "show":
                view.startAnimation(bigShowAnim);
                view.setVisibility(View.VISIBLE);
                break;
            case "hide":
                view.startAnimation(smallHideAnim);
                smallHideAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_map_weather;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();//生命周期管理
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();//生命周期管理
    }


    @Override
    protected MapWeatherContract.MapWeatherPresenter createPresent() {
        return new MapWeatherContract.MapWeatherPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();// 退出时销毁定位
        mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
        mapView.onDestroy();// 在activity执行onDestroy时必须调用mMapView.onDestroy()
    }

}