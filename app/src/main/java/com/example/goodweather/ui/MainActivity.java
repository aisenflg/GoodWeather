package com.example.goodweather.ui;

import android.animation.Animator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.goodweather.R;
import com.example.goodweather.adapter.AreaAdapter;
import com.example.goodweather.adapter.CityAdapter;
import com.example.goodweather.adapter.DailyAdapter;
import com.example.goodweather.adapter.HourlyAdapter;
import com.example.goodweather.adapter.MainChangeCommonlyCityAdapter;
import com.example.goodweather.adapter.MinutePrecAdapter;
import com.example.goodweather.adapter.ProvinceAdapter;
import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.CityBean;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.LifestyleResponse;
import com.example.goodweather.bean.MinutePrecResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.goodweather.bean.WarningResponse;
import com.example.goodweather.contract.WeatherContract;
import com.example.goodweather.eventbus.SearchCityEvent;
import com.example.goodweather.utils.APKVersionInfoUtils;
import com.example.goodweather.utils.AppStartUpUtils;
import com.example.goodweather.utils.CodeToStringUtils;
import com.example.goodweather.utils.Constant;
import com.example.goodweather.utils.DateUtils;
import com.example.goodweather.utils.SPUtils;
import com.example.goodweather.utils.StatusBarUtil;
import com.example.goodweather.utils.ToastUtils;
import com.example.goodweather.utils.WeatherUtil;
import com.example.mvplibrary.bean.AppVersion;
import com.example.mvplibrary.bean.ResidentCity;
import com.example.mvplibrary.mvp.MvpActivity;
import com.example.mvplibrary.utils.AnimationUtil;
import com.example.mvplibrary.utils.LiWindow;
import com.example.mvplibrary.utils.SizeUtils;
import com.example.mvplibrary.view.RoundProgressBar;
import com.example.mvplibrary.view.WhiteWindmills;
import com.example.mvplibrary.view.dialog.AlertDialog;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

import static com.example.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimation;
import static com.example.mvplibrary.utils.RecyclerViewAnimation.runLayoutAnimationRight;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter>
        implements WeatherContract.IWeatherView, View.OnClickListener, View.OnScrollChangeListener {

    @BindView(R.id.bg)
    ImageView bg;//背景图
    @BindView(R.id.tv_week)
    TextView tvWeek;//星期
    @BindView(R.id.tv_warn)
    TextView tvWarn;//灾害预警
    @BindView(R.id.tv_air_info)
    TextView tvAirInfo;//空气质量
    @BindView(R.id.tv_info)
    TextView tvInfo;//天气状况
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;//温度
    @BindView(R.id.tv_temp_height)
    TextView tvTempHeight;//最高温
    @BindView(R.id.tv_temp_low)
    TextView tvTempLow;//最低温
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
    @BindView(R.id.tv_uv)
    TextView tvUv;//紫外线
    @BindView(R.id.tv_title)
    TextView tvTitle;//标题
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;//紫外线
    @BindView(R.id.lay_slide_area)
    LinearLayout laySlideArea;//紫外线
    @BindView(R.id.tv_more_daily)
    TextView tvMoreDaily;//更多天气预报
    @BindView(R.id.tv_more_air)
    TextView tvMoreAir;//更多空气信息
    @BindView(R.id.tv_more_lifestyle)
    TextView tvMoreLifestyle;//更多生活建议
    @BindView(R.id.iv_map)
    ImageView ivMap;//地图
    @BindView(R.id.tv_precipitation)
    TextView tvPrecipitation;//降水预告
    @BindView(R.id.tv_prec_more)
    TextView tvPrecMore;//降水详情
    @BindView(R.id.rv_prec_detail)
    RecyclerView rvPrecDetail;//分钟级降水列表
    @BindView(R.id.rv_change_city)
    RecyclerView rvChangeCity;//点击切换常用城市


    private String stationName = null;//空气质量站点 查询空气质量站点才需要，


    private boolean flag = true;//定位图标显示,只有定位的时候显示为true,切换城市和常用城市不显示
    private String warnBodyString = null;//灾害预警数据字符串

    private boolean changeCityState = false;//常用城市列表  收缩状态  false 收缩  true 展开
    private boolean isChangeCity = false;//是否可以展开，如果没有添加常用城市，自然不能展开
    //主页面切换城市列表适配器
    private MainChangeCommonlyCityAdapter changeCityAdapter;
    //常用城市切换列表
    private List<ResidentCity> residentCityList = new ArrayList<>();


    //定位器
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    //V7 版本
    List<DailyResponse.DailyBean> dailyListV7 = new ArrayList<>();//天气预报数据列表
    DailyAdapter mAdapterDailyV7;//天气预报适配器
    List<HourlyResponse.HourlyBean> hourlyListV7 = new ArrayList<>();//逐小时天气预报数据列表
    HourlyAdapter mAdapterHourlyV7;//逐小时预报适配器
    private List<MinutePrecResponse.MinutelyBean> minutelyList = new ArrayList<>();//分钟级降水数据列表
    private MinutePrecAdapter mAdapterMinutePrec;//分钟级降水适配器

    private boolean state = false;//分钟级降水数据 收缩状态  false 收缩  true 展开


    private String lon = null;//经度
    private String lat = null;//纬度
    private List<String> list;//字符串列表
    private List<CityBean> provinceList;//省列表数据
    private List<CityBean.Citydata> citylist;//市列表数据
    private List<CityBean.Citydata.AreaBean> arealist;//区/县列表数据
    private String district; //区/县 更改为全局变量,方便更换城市后也能下拉刷新
    private String city; //市  国控站点数据,用于请求空气质量
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
    private boolean flagOther = false;//跳转其他页面时才为true
    private AlertDialog updateAppDialog = null;//应用更新提示弹窗
    private AlertDialog everyDayTipDialog = null;//每日弹窗
    private String dialogTemp = null;//弹窗中的今日温度
    private String dialogWeatherState = null;//弹窗中的天气状态
    private int dialogWeatherStateCode = 0;//弹窗中的天气状态码
    private String dialogPrecipitation = null;//弹窗中的降水预告
    private int dialogTempHeight = 0;//弹窗中今日最高温
    private int dialogTempLow = 0;//弹窗中今日最低温

    private String locationId = null;//城市id，用于查询城市数据  V7版本 中 才有

    private int OPEN_LOCATION = 9527;//进入手机定位设置页面标识


    //数据初始化  主线程，onCreate方法可以删除了，把里面的代码移动这个initData下面
    @Override
    public void initData(Bundle savedInstanceState) {
        //因为这个框架里面已经放入了绑定，所以这行代码可以注释掉了。
        //ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(context);//透明状态栏
        initList();
        //是否开启定位权限
        if (isOpenLocationServiceEnable()) {
//            tvCity.setEnabled(false);//不可点击
            startLocation();//开始定位
        } else {
//            tvCity.setEnabled(true);//可以点击
            ToastUtils.showShortToast(context, "(((φ(◎ロ◎;)φ)))，你好像忘记打开定位功能了");
            tvCity.setText("打开定位");
        }

        startLocation();
        //禁用上拉刷新
        refresh.setEnableLoadMore(false);
        //初始化弹窗
        mPopupWindow = new PopupWindow(this);
        mAnimationUtil = new AnimationUtil();
        setListener();
        EventBus.getDefault().register(this);//注册
        scrollView.setOnScrollChangeListener(this);
        //加载常用城市
        loadingCommonlyUsedCity();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchCityEvent event) {//接收
        loadingCommonlyUsedCity();
        mPresent.newSearchCity(event.mLocation);
    }


    private void setListener() {
        ivAdd.setOnClickListener(this);
        tvMoreAir.setOnClickListener(this);
        tvMoreLifestyle.setOnClickListener(this);
        tvMoreDaily.setOnClickListener(this);
        ivMap.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvPrecMore.setOnClickListener(this);
    }

    //绑定布局文件
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initList() {
        /**   V7 版本   **/
        //天气预报  7天
        mAdapterDailyV7 = new DailyAdapter(R.layout.item_weather_forecast_list, dailyListV7);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapterDailyV7);

        //逐小时天气预报  24小时
        mAdapterHourlyV7 = new HourlyAdapter(R.layout.item_weather_hourly_list, hourlyListV7);
        LinearLayoutManager managerHourly = new LinearLayoutManager(context);
        managerHourly.setOrientation(RecyclerView.HORIZONTAL);//设置列表为横向
        rvHourly.setLayoutManager(managerHourly);
        rvHourly.setAdapter(mAdapterHourlyV7);

        //分钟级降水
        mAdapterMinutePrec = new MinutePrecAdapter(R.layout.item_prec_detail_list, minutelyList);
        GridLayoutManager managerMinutePrec = new GridLayoutManager(context, 2);
        managerMinutePrec.setOrientation(RecyclerView.HORIZONTAL);
        rvPrecDetail.setLayoutManager(managerMinutePrec);
        rvPrecDetail.setAdapter(mAdapterMinutePrec);

        //天气预报列表item点击事件
        mAdapterDailyV7.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                DailyResponse.DailyBean data = dailyListV7.get(position);
                showForecastWindow(data);
            }
        });


        //逐小时天气预报列表item点击事件
        mAdapterHourlyV7.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //赋值
                HourlyResponse.HourlyBean data = hourlyListV7.get(position);
                //小时天气详情弹窗
                showHourlyWindow(data);
            }
        });


    }

    //显示天气预报详情弹窗
    private void showForecastWindow(DailyResponse.DailyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_forecast_detail, null);
        TextView tv_datetime = view.findViewById(R.id.tv_datetime);
        TextView tv_tmp_max = view.findViewById(R.id.tv_tmp_max);//最高温
        TextView tv_tmp_min = view.findViewById(R.id.tv_tmp_min);//最低温
        TextView tv_uv_index = view.findViewById(R.id.tv_uv_index);//紫外线强度
        TextView tv_cond_txt_d = view.findViewById(R.id.tv_cond_txt_d);//白天天气状态
        TextView tv_cond_txt_n = view.findViewById(R.id.tv_cond_txt_n);//晚上天气状态
        TextView tv_wind_deg = view.findViewById(R.id.tv_wind_deg);//风向360角度
        TextView tv_wind_dir = view.findViewById(R.id.tv_wind_dir);//风向
        TextView tv_wind_sc = view.findViewById(R.id.tv_wind_sc);//风力
        TextView tv_wind_spd = view.findViewById(R.id.tv_wind_spd);//风速
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);//云量  V7 新增
        TextView tv_hum = view.findViewById(R.id.tv_hum);//相对湿度
        TextView tv_pres = view.findViewById(R.id.tv_pres);//大气压强
        TextView tv_pcpn = view.findViewById(R.id.tv_pcpn);//降水量
//        TextView tv_pop = view.findViewById(R.id.tv_pop);//降水概率   V7删除
        TextView tv_vis = view.findViewById(R.id.tv_vis);//能见度

        tv_datetime.setText(data.getFxDate() + "   " + DateUtils.Week(data.getFxDate()));//时间日期
        tv_tmp_max.setText(data.getTempMax() + "℃");
        tv_tmp_min.setText(" / " + data.getTempMin() + "℃");
        tv_uv_index.setText(data.getUvIndex());
        tv_cond_txt_d.setText(data.getTextDay());
        tv_cond_txt_n.setText(data.getTextNight());
        tv_wind_deg.setText(data.getWind360Day() + "°");
        tv_wind_dir.setText(data.getWindDirDay());
        tv_wind_sc.setText(data.getWindScaleDay() + "级");
        tv_wind_spd.setText(data.getWindSpeedDay() + "公里/小时");
        tv_cloud.setText(data.getCloud() + "%");//V7 版本中，新增 云量
        tv_hum.setText(data.getHumidity() + "%");
        tv_pres.setText(data.getPressure() + "hPa");
        tv_pcpn.setText(data.getPrecip() + "mm");
//        tv_pop.setText(data.getPop() + "%");//V7 版本中，天气预报中没有降水概率这个值
        tv_vis.setText(data.getVis() + "km");

        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 500), true);

    }


    //显示逐三小时详情天气信息弹窗
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showHourlyWindow(HourlyResponse.HourlyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_hourly_detail, null);
        TextView tv_time = view.findViewById(R.id.tv_time);//时间
        TextView tv_tem = view.findViewById(R.id.tv_tem);//温度
        TextView tv_cond_txt = view.findViewById(R.id.tv_cond_txt);//天气状态描述
        TextView tv_wind_deg = view.findViewById(R.id.tv_wind_deg);//风向360角度
        TextView tv_wind_dir = view.findViewById(R.id.tv_wind_dir);//风向
        TextView tv_wind_sc = view.findViewById(R.id.tv_wind_sc);//风力
        TextView tv_wind_spd = view.findViewById(R.id.tv_wind_spd);//风速
        TextView tv_hum = view.findViewById(R.id.tv_hum);//相对湿度
        TextView tv_pres = view.findViewById(R.id.tv_pres);//大气压强
        TextView tv_pop = view.findViewById(R.id.tv_pop);//降水概率
        TextView tv_dew = view.findViewById(R.id.tv_dew);//露点温度
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);//云量

        String time = DateUtils.updateTime(data.getFxTime());
        tv_time.setText(WeatherUtil.showTimeInfo(time) + time);
        tv_tem.setText(data.getTemp() + "℃");
        tv_cond_txt.setText(data.getText());
        tv_wind_deg.setText(data.getWind360() + "°");
        tv_wind_dir.setText(data.getWindDir());
        tv_wind_sc.setText(data.getWindScale() + "级");
        tv_wind_spd.setText(data.getWindSpeed() + "公里/小时");
        tv_hum.setText(data.getHumidity() + "%");
        tv_pres.setText(data.getPressure() + "hPa");
        tv_pop.setText(data.getPop() + "%");
        tv_dew.setText(data.getDew() + "℃");
        tv_cloud.setText(data.getCloud() + "%");

        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 400), true);
    }

    //绑定Presenter ，这里不绑定会报错
    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return new WeatherContract.WeatherPresenter();
    }


    /**
     * 手机是否开启位置服务，如果没有开启那么App将不能使用定位功能
     */
    private boolean isOpenLocationServiceEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
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
    private void showAddWindow() {
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
        TextView changeCity = mPopupWindow.getContentView().findViewById(R.id.tv_change_city);//切换城市
        TextView changeBg = mPopupWindow.getContentView().findViewById(R.id.tv_change_bg);  //切换背景  已弃用
        TextView wallpaper = mPopupWindow.getContentView().findViewById(R.id.tv_wallpaper);  //壁纸管理
        TextView searchCity = mPopupWindow.getContentView().findViewById(R.id.tv_search_city);  //搜索城市
        TextView hotCity = mPopupWindow.getContentView().findViewById(R.id.world_city);  //世界城市
        TextView oftenCity = mPopupWindow.getContentView().findViewById(R.id.tv_often_city);  //常用城市
        TextView more = mPopupWindow.getContentView().findViewById(R.id.tv_more);//更多建议
        TextView setting = mPopupWindow.getContentView().findViewById(R.id.tv_setting);//更多建议
        changeCity.setOnClickListener(view -> {//切换城市
            showCityWindow();
            mPopupWindow.dismiss();
        });
//        changeBg.setOnClickListener(view -> {//切换背景
//            //放入缓存
//            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, true, context);
//            SPUtils.putString(Constant.DISTRICT, district, context);
//            SPUtils.putString(Constant.CITY, city, context);
//            startActivity(new Intent(MainActivity.this, BackgroundManagerActivity.class));
//            mPopupWindow.dismiss();
//        });

        wallpaper.setOnClickListener(view -> {//壁纸管理
            startActivity(new Intent(context, WallPaperActivity.class));
            mPopupWindow.dismiss();
        });

        searchCity.setOnClickListener(new View.OnClickListener() {//搜索城市
            @Override
            public void onClick(View v) {
                SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//缓存标识
                startActivity(new Intent(context, SearchCityActivity.class));
                mPopupWindow.dismiss();
            }
        });
        hotCity.setOnClickListener(new View.OnClickListener() {//热门城市
            @Override
            public void onClick(View v) {
                SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, true, context);//缓存标识
                startActivity(new Intent(context, WorldCityActivity.class));
                mPopupWindow.dismiss();
            }
        });
        //常用城市
        oftenCity.setOnClickListener(v -> {
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//缓存标识
            startActivity(new Intent(context, CommonlyUsedCityActivity.class));
            mPopupWindow.dismiss();
        });
        more.setOnClickListener(view -> {//更多功能
            ToastUtils.showShortToast(context, "如果你有什么好的建议，可以博客留言哦！");
            mPopupWindow.dismiss();
        });
        setting.setOnClickListener(v -> {
            startActivity(new Intent(this,SettingActivity.class));
            mPopupWindow.dismiss();
        });

    }


    /**
     * 计算动画时间
     */
    private void toggleBright() {
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
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    /**
     * 加载常用城市数据
     */
    private void loadingCommonlyUsedCity() {
        residentCityList = LitePal.findAll(ResidentCity.class);
        //先判断是否有常用城市
        if (residentCityList != null && residentCityList.size() > 0) {
            isChangeCity = true;
        } else {
            isChangeCity = false;
        }
        //配置适配器
        changeCityAdapter = new MainChangeCommonlyCityAdapter(R.layout.item_main_city_change, residentCityList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rvChangeCity.setLayoutManager(manager);
        rvChangeCity.setAdapter(changeCityAdapter);
        //常用城市点击
        changeCityAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            showLoadingDialog();
            district = residentCityList.get(position).getLocation();
            mPresent.newSearchCity(district);
            flag = false;
            //隐藏列表
            rvChangeCity.setVisibility(View.GONE);
            changeCityState = false;
        });
    }


    /**
     * 添加到常用城市列表
     *
     * @param locationBean
     */
    private void addCommonlyUsedCity(NewSearchCityResponse.LocationBean locationBean) {
        if (flag) {//定位到的城市
            List<ResidentCity> residentCityList = LitePal.findAll(ResidentCity.class);
            if (residentCityList != null && residentCityList.size() > 0) {
                //查询要添加的城市是否已经存在
                List<ResidentCity> residentCities = LitePal
                        .where("location = ? and parent_city = ?", locationBean.getName(), locationBean.getAdm2())
                        .find(ResidentCity.class);
                if (residentCities.size() == 0) {
                    ResidentCity residentCity = new ResidentCity();
                    residentCity.setLocation(locationBean.getName());//地区／城市名称
                    residentCity.setParent_city(locationBean.getAdm2());//该地区／城市的上级城市
                    residentCity.setAdmin_area(locationBean.getAdm1());//该地区／城市所属行政区域
                    residentCity.setCnty(locationBean.getCountry());//该地区／城市所属国家名称
                    residentCity.save();//保存数据到数据库中
                }
            } else {
                ResidentCity residentCity = new ResidentCity();
                residentCity.setLocation(locationBean.getName());//地区／城市名称
                residentCity.setParent_city(locationBean.getAdm2());//该地区／城市的上级城市
                residentCity.setAdmin_area(locationBean.getAdm1());//该地区／城市所属行政区域
                residentCity.setCnty(locationBean.getCountry());//该地区／城市所属国家名称
                residentCity.save();//保存数据到数据库中
            }

        }

        //加载常用城市数据
        loadingCommonlyUsedCity();
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
                                            district = arealist.get(position).getName();//选中的区/县赋值给这个全局变量
                                            //在V7版本中需要先拿到城市ID,在结果返回中值中再进行下一步处理
                                            mPresent.newSearchCity(district);
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
            //更多天气数据
            case R.id.tv_more_daily:
                goToMore(MoreDailyActivity.class);
                break;
            //更多空气质量
            case R.id.tv_more_air:
                goToMore(MoreAirActivity.class);
                break;
            //更多生活建议
            case R.id.tv_more_lifestyle:
                goToMore(MoreLifestyleActivity.class);
                break;
            //灾害预警
            case R.id.tv_warn:
                Intent intent = new Intent(context, WarnActivity.class);
                intent.putExtra("warnBodyString", warnBodyString);
                startActivity(intent);
                break;
            //地图天气
            case R.id.iv_map:
                if (!isOpenLocationServiceEnable()) {
                    ToastUtils.showShortToast(context, "请打开定位权限");
                    return;
                }
                startActivity(new Intent(context, MapWeatherActivity.class));
                break;
            case R.id.tv_city: //当用户没有打开GPS定位时,可以点击这个TextView重新定位
                if (isOpenLocationServiceEnable()) {
                    //已开启定位
                    if (tvCity.getText().toString().contains("定位")) {
                        tvCity.setText("定位中");
                        startLocation();
                    }else {
                        if (isChangeCity) {
                            //有数据
                            if (changeCityState) {
                                //收缩
                                rvChangeCity.setVisibility(View.GONE);
                                changeCityState = false;
                            }else {
                                rvChangeCity.setVisibility(View.VISIBLE);
                                changeCityState = true;
                            }
                        }
                    }
                } else {
                    //跳转到系统定位设置
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), OPEN_LOCATION);
                }
                break;
            case R.id.tv_prec_more://分钟级降水收缩
                if (state) {
                    //收缩
                    AnimationUtil.collapse(rvPrecDetail, tvPrecMore);
                    state = false;
                } else {
                    //展开
                    AnimationUtil.expand(rvPrecDetail, tvPrecMore);
                    state = true;
                }

        }
    }

    /**
     * 返回Activity的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == OPEN_LOCATION) {//则是从手机定位页面返回
            if (isOpenLocationServiceEnable()) {//已打开
                tvCity.setText("重新定位");
                tvCity.setEnabled(true);//可以点击
            } else {
                ToastUtils.showShortToast(context, "有意思吗？你跳过去又不打开定位，玩呢？嗯？我也是有脾气的好伐！");
                tvCity.setText("打开定位");
                tvCity.setEnabled(false);//不可点击
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 进入更多数据页面
     *
     * @param clazz 要进入的页面
     */
    private void goToMore(Class<?> clazz) {
        if (locationId == null) {
            ToastUtils.showShortToast(context, "很抱歉，未获取到相关更多信息");
        } else {
            Intent intent = new Intent(context, clazz);
            intent.putExtra("locationId", locationId);
            intent.putExtra("stationName", stationName);
            intent.putExtra("cityName", tvCity.getText().toString());
            startActivity(intent);

        }

    }


    /**
     * 滑动监听
     *
     * @param v
     * @param scrollX
     * @param scrollY
     * @param oldScrollX
     * @param oldScrollY
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollX > oldScrollY) {
            Log.e("onScroll", "上滑");
            //laySlideArea.getMeasuredHeight() 表示控件的绘制高度
            if (scrollY > laySlideArea.getMeasuredHeight()) {
                String tx = tvCity.getText().toString();
                if (tx.contains("定位中")) {//因为存在网络异常问题，总不能你没有城市，还给你改变UI吧
                    tvTitle.setText("城市天气");
                } else {
                    tvTitle.setText(tx);//改变TextView的显示文本
                }
            }
            if (scrollY > oldScrollY) {
                Log.e("onScroll", "下滑");
                if (scrollY < laySlideArea.getMeasuredHeight()) {
                    tvTitle.setText("城市天气");//改回原来的
                }
            }
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
            if (district == null) {
                //未获取到定位信息,请重新定位
                ToastUtils.showShortToast(context, "未获取到定位信息,请重新定位");
                tvCity.setText("重新定位");
//                tvCity.setEnabled(true);//可点击
            } else {
                //在数据请求之前加载等待弹窗,结果返回后关闭
                showLoadingDialog();
                //在V7版本中需要先拿到城市ID,在结果返回中值中再进行下一步处理
                mPresent.newSearchCity(district);

                //下拉刷新
                refresh.setOnRefreshListener(refreshLayout -> {
                    //在V7版本中需要先拿到城市ID,在结果返回中值中再进行下一步处理
                    mPresent.newSearchCity(district);
                });

            }
        }
    }


    /**
     * 应用更新提示弹窗
     *
     * @param downloadUrl 下载地址
     * @param updateLog   更新日志
     */
    private void showUpdateAppDialog(String downloadUrl, String updateLog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .addDefaultAnimation()//默认弹窗动画
                .setCancelable(true)
                .setText(R.id.tv_update_info, updateLog)
                .setContentView(R.layout.dialog_update_app_tip)//载入布局文件
                .setWidthAndHeight(SizeUtils.dp2px(context, 270), ViewGroup.LayoutParams.WRAP_CONTENT)//设置弹窗宽高
                .setOnClickListener(R.id.tv_cancel, v -> {//取消
                    updateAppDialog.dismiss();
                }).setOnClickListener(R.id.tv_fast_update, v -> {//立即更新
                    //开始下载
                    downloadApk(downloadUrl);
                    updateAppDialog.dismiss();
                });
        updateAppDialog = builder.create();
        updateAppDialog.show();
    }

    /**
     * 清除APK
     *
     * @param apkName
     * @return
     */
    public static File clearApk(String apkName) {
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), apkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }
        return apkFile;
    }

    /**
     * 下载APK
     *
     * @param downloadUrl
     */
    private void downloadApk(String downloadUrl) {
        clearApk("GoodWeather.apk");
        //下载管理器 获取系统下载服务
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        //设置运行使用的网络类型，移动网络或者Wifi都可以
        request.setAllowedNetworkTypes(request.NETWORK_MOBILE | request.NETWORK_WIFI);
        //设置是否允许漫游
        request.setAllowedOverRoaming(true);
        //设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
        request.setMimeType(mimeString);
        //设置下载时或者下载完成时，通知栏是否显示
        request.setNotificationVisibility(request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载新版本");
        request.setVisibleInDownloadsUi(true);//下载UI
        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "GoodWeather.apk");
        //将下载请求放入队列
        downloadManager.enqueue(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();//在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
        flagOther = SPUtils.getBoolean(Constant.FLAG_OTHER_RETURN, false, context);
        if (flagOther == true) {
            //取出缓存
            district = SPUtils.getString(Constant.DISTRICT, "", context);
            city = SPUtils.getString(Constant.CITY, "", context);
            //在V7版本中需要先拿到城市ID,在结果返回中值中再进行下一步处理
            mPresent.newSearchCity(district);
        } else {
            dismissLoadingDialog();
        }
        updateWallpaper();
    }


    /**
     * 更换壁纸
     */
    private void updateWallpaper() {
        String imgUrl = SPUtils.getString(Constant.WALLPAPER_URL, null, context);
        if (imgUrl != null) {
            Glide.with(context).load(imgUrl).into(bg);
        } else {
            Glide.with(context).load(R.drawable.img_5).into(bg);
        }
    }


//    //判断是否开启了切换背景，没有开启则用默认的背景
//    private void isOpenChangeBg() {
//        boolean isEverydayImg = SPUtils.getBoolean(Constant.EVERYDAY_IMG, false, context);//每日图片
//        boolean isImgList = SPUtils.getBoolean(Constant.IMG_LIST, false, context);//图片列表
//        boolean isCustomImg = SPUtils.getBoolean(Constant.CUSTOM_IMG, false, context);//手动定义
//        //因为只有有一个为true，其他两个就都会是false,所以可以一个一个的判断
//        if (isEverydayImg != true && isImgList != true && isCustomImg != true) {
//            //当所有开关都没有打开的时候用默认的图片
//            bg.setBackgroundResource(R.mipmap.pic_bg);
//        } else {
//            if (isEverydayImg != false) {//开启每日一图
//                mPresent.biying(context);
//            } else if (isImgList != false) {//开启图片列表
//                int position = SPUtils.getInt(Constant.IMG_POSITION, -1, context);
//                switch (position) {
//                    case 0:
//                        bg.setBackgroundResource(R.drawable.img_1);
//                        break;
//                    case 1:
//                        bg.setBackgroundResource(R.drawable.img_2);
//                        break;
//                    case 2:
//                        bg.setBackgroundResource(R.drawable.img_3);
//                        break;
//                    case 3:
//                        bg.setBackgroundResource(R.drawable.img_4);
//                        break;
//                    case 4:
//                        bg.setBackgroundResource(R.drawable.img_5);
//                        break;
//                    case 5:
//                        bg.setBackgroundResource(R.drawable.img_6);
//                        break;
//                }
//            } else if (isCustomImg) {
//                String imgPath = SPUtils.getString(Constant.CUSTOM_IMG_PATH, "", context);
//                Glide.with(context).load(imgPath).into(bg);
//            }
//        }
//    }


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


    /**
     * 和风天气  V7  API
     * 通过定位到的地址 /  城市切换得到的地址  都需要查询出对应的城市id才行，所以在V7版本中，这是第一步接口
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        refresh.finishRefresh();//关闭刷新
        dismissLoadingDialog();//关闭弹窗
        if (mLocationClient != null) {
            mLocationClient.stop();//数据返回后关闭定位
        }
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            if (response.body().getLocation() != null && response.body().getLocation().size() > 0) {
                NewSearchCityResponse.LocationBean locationBean = response.body().getLocation().get(0);
                //如果是定位到当前地方则添加到常用城市
                addCommonlyUsedCity(locationBean);
                tvCity.setText(response.body().getLocation().get(0).getName());//城市
                locationId = response.body().getLocation().get(0).getId();//城市Id
                stationName = response.body().getLocation().get(0).getAdm2(); //上级城市,也是空气质量站点
                lon = response.body().getLocation().get(0).getLon();//经度
                lat = response.body().getLocation().get(0).getLat();//纬度
                showLoadingDialog();
                mPresent.nowWeather(locationId);//查询实况天气
                mPresent.dailyWeather(locationId);//查询天气预报
                mPresent.hourlyWeather(locationId);//查询逐小时天气预报
                mPresent.airNowWeather(locationId);//空气质量
                mPresent.lifestyle(locationId);//生活指数
                mPresent.getWarning(locationId);//灾害预警
                mPresent.getMinutePrec(lon + "," + lat);
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            tvCity.setText("查询城市失败");
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    /**
     * 实况天气数据返回  V7
     *
     * @param response
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void getNowResult(Response<NowResponse> response) {
        dismissLoadingDialog();
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {//200则成功返回数据
            //根据V7版本的原则，只要是200就一定有数据，我们可以不用做判空处理，但是，为了使程序不ANR，还是要做的，信自己得永生
            NowResponse data = response.body();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
            tvTemperature.setText(data.getNow().getTemp());//温度

            dialogTemp = data.getNow().getTemp();
            dialogWeatherState = data.getNow().getText();
            dialogWeatherStateCode = Integer.parseInt(data.getNow().getIcon());

            tvTemperature.setTypeface(typeface);//使用字体
            if (data != null) {
                tvTemperature.setText(data.getNow().getTemp());//温度
                if (flag) {
                    ivLocation.setVisibility(View.VISIBLE);//显示定位图标
                } else {
                    ivLocation.setVisibility(View.GONE);//显示定位图标
                }
                tvWeek.setText(DateUtils.getWeekOfDate(new Date()));//星期
                tvInfo.setText(data.getNow().getText());
                String time = DateUtils.updateTime(data.getUpdateTime());//截去前面的字符，保留后面所有的字符，就剩下 22:00

                tvOldTime.setText("上次更新时间：" + WeatherUtil.showTimeInfo(time) + time);
                tvWindDirection.setText("风向     " + data.getNow().getWindDir());//风向
                tvWindPower.setText("风力     " + data.getNow().getWindScale() + "级");//风力
                wwBig.startRotate();//大风车开始转动
                wwSmall.startRotate();//小风车开始转动
            } else {
                ToastUtils.showShortToast(context, "暂无实况天气数据");
            }
        } else {//其他状态返回提示文字
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    /**
     * 天气预报数据返回  V7
     *
     * @param response
     */
    @Override
    public void getDailyResult(Response<DailyResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyBean> data = response.body().getDaily();
            if (data != null && data.size() > 0) {//判空处理
                tvTempHeight.setText(data.get(0).getTempMax() + "℃");
                tvTempLow.setText("/" + data.get(0).getTempMin() + "℃");
                dialogTempHeight = Integer.parseInt(data.get(0).getTempMax());
                dialogTempLow = Integer.parseInt(data.get(0).getTempMin());
                dailyListV7.clear();//添加数据之前先清除
                dailyListV7.addAll(data);//添加数据
                mAdapterDailyV7.notifyDataSetChanged();//刷新列表
                runLayoutAnimation(rv);//底部动画展示
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {//异常状态码返回
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    /**
     * 逐小时天气数据返回  V7
     *
     * @param response
     */
    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<HourlyResponse.HourlyBean> data = response.body().getHourly();
            if (data != null && data.size() > 0) {
                hourlyListV7.clear();
                hourlyListV7.addAll(data);
                mAdapterHourlyV7.notifyDataSetChanged();
                runLayoutAnimationRight(rvHourly);
            } else {
                ToastUtils.showShortToast(context, "逐小时预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    /**
     * 空气质量返回  V7
     *
     * @param response
     */
    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            AirNowResponse.NowBean data = response.body().getNow();
            if (response.body().getNow() != null) {
                rpbAqi.setMaxProgress(300);//最大进度，用于计算
                rpbAqi.setMinText("0");//设置显示最小值
                rpbAqi.setMinTextSize(32f);
                rpbAqi.setMaxText("300");//设置显示最大值
                rpbAqi.setMaxTextSize(32f);
                rpbAqi.setProgress(Float.valueOf(data.getAqi()));//当前进度
                rpbAqi.setArcBgColor(getResources().getColor(R.color.arc_bg_color));//圆弧的颜色
                rpbAqi.setProgressColor(getResources().getColor(R.color.arc_progress_color));//进度圆弧的颜色
                rpbAqi.setFirstText(data.getCategory());//空气质量描述 取值范围：优，良，轻度污染，中度污染，重度污染，严重污染
                rpbAqi.setFirstTextSize(44f);//第一行文本的字体大小
                rpbAqi.setSecondText(data.getAqi());//空气质量值
                rpbAqi.setSecondTextSize(64f);//第二行文本的字体大小
                rpbAqi.setMinText("0");
                rpbAqi.setMinTextColor(getResources().getColor(R.color.arc_progress_color));

                tvAirInfo.setText("空气" + data.getCategory());
                tvPm10.setText(data.getPm10());//PM10
                tvPm25.setText(data.getPm2p5());//PM2.5
                tvNo2.setText(data.getNo2());//二氧化氮
                tvSo2.setText(data.getSo2());//二氧化硫
                tvO3.setText(data.getO3());//臭氧
                tvCo.setText(data.getCo());//一氧化碳
            } else {
                ToastUtils.showShortToast(context, "空气质量数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    /**
     * 生活数据返回 V7
     *
     * @param response
     */
    @Override
    public void getLifestyleResult(Response<LifestyleResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<LifestyleResponse.DailyBean> data = response.body().getDaily();
            for (int i = 0; i < data.size(); i++) {
                switch (data.get(i).getType()) {
                    case "5":
                        tvUv.setText("紫外线：" + data.get(i).getText());
                        break;
                    case "8":
                        tvComf.setText("舒适度：" + data.get(i).getText());
                        break;
                    case "3":
                        tvDrsg.setText("穿衣指数：" + data.get(i).getText());
                        break;
                    case "9":
                        tvFlu.setText("感冒指数：" + data.get(i).getText());
                        break;
                    case "1":
                        tvSport.setText("运动指数：" + data.get(i).getText());
                        break;
                    case "6":
                        tvTrav.setText("旅游指数：" + data.get(i).getText());
                        break;
                    case "2":
                        tvCw.setText("洗车指数：" + data.get(i).getText());
                        break;
                    case "10":
                        tvAir.setText("空气指数：" + data.get(i).getText());
                        break;
                }
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 灾害预警返回
     *
     * @param response
     */
    @Override
    public void getWarningResult(Response<WarningResponse> response) {
        dismissLoadingDialog();//关闭弹窗
        mPresent.getAppVersion(Constant.APIKEY, Constant.APPKEY);//检查更新
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<WarningResponse.WarningBean> data = response.body().getWarning();
            if (data != null && data.size() > 0) {
                tvWarn.setText(data.get(0).getTitle() + "   " + data.get(0).getText());
                tvWarn.setVisibility(View.VISIBLE);
                warnBodyString = new Gson().toJson(response.body());
            } else {
                //没有数据则隐藏跑马灯
                tvWarn.setVisibility(View.GONE);
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    @Override
    public void getAppVersionResult(Response<AppVersion> response) {
        if (response.body().getData() != null) {
            if (AppStartUpUtils.isTodayFirstStartApp(context)) {//今天第一次打开APP
            if (!response.body().getData().getBuildVersion().equals(APKVersionInfoUtils.getVerName(context))) {//提示更新
                    //更新提示弹窗
                    showUpdateAppDialog(response.body().getData().getDownloadURL(), response.body().getData().getBuildUpdateDescription());
                }
                //设置每日提示弹窗
                setTipDialog();
            }
        }
    }

    /**
     * 设置每日弹窗
     */
    private void setTipDialog() {
        boolean isShow = SPUtils.getBoolean(Constant.EVERYDAY_POP_BOOLEAN, true, context);
        if (isShow) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //当所有数据加载完成之后显示弹窗
                    if (everyDayTipDialog != null) {
                        return;
                    }
                    //弹出每日提醒弹窗
                    showEveryDayTipDialog();
                }
            },1000);
        }
    }

    /**
     * 每日提示弹窗
     */
    private void showEveryDayTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .addDefaultAnimation()//默认弹窗动画
                .setCancelable(false)
                .setText(R.id.tv_week, DateUtils.getWeekOfDate(new Date()))//星期
                .setText(R.id.tv_weather_state, dialogWeatherState)//天气状态
                .setText(R.id.tv_precipitation, dialogPrecipitation)//降水预告
                .setText(R.id.tv_temp_difference,
                        WeatherUtil.differenceTempTip(dialogTempHeight, dialogTempLow))//温差提示信息
                .setContentView(R.layout.dialog_everyday_tip)//载入布局文件
                .setWidthAndHeight(SizeUtils.dp2px(context, 270), ViewGroup.LayoutParams.WRAP_CONTENT)//设置弹窗宽高
                .setOnClickListener(R.id.iv_close, v -> {//关闭
                    everyDayTipDialog.dismiss();
                });
        everyDayTipDialog = builder.create();
        String imgUrl = SPUtils.getString(Constant.EVERYDAY_TIP_IMG, "", context);
        ShapeableImageView bg = everyDayTipDialog.getView(R.id.iv_dialog_bg);
        Glide.with(context).load(imgUrl).into(bg);
        //温度
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        TextView temp = everyDayTipDialog.getView(R.id.tv_temperature);
        temp.setTypeface(typeface);
        temp.setText(dialogTemp + "℃");
        //设置天气状态图标
        ImageView weatherStateIcon = everyDayTipDialog.getView(R.id.iv_weather_state);
        WeatherUtil.changeIcon(weatherStateIcon, dialogWeatherStateCode);

        //不再弹出
        MaterialCheckBox cb = everyDayTipDialog.getView(R.id.cb_no_pop_up);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtils.putBoolean(Constant.EVERYDAY_POP_BOOLEAN, false, context);
                } else {
                    SPUtils.putBoolean(Constant.EVERYDAY_POP_BOOLEAN, true, context);
                }
            }
        });

        everyDayTipDialog.show();
    }



    /**
     * 分钟级降水
     */
    @Override
    public void getMinutePrecResult(Response<MinutePrecResponse> response) {
        dismissLoadingDialog();//关闭加载弹窗
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            tvPrecipitation.setText(response.body().getSummary());
            dialogPrecipitation = response.body().getSummary();
            if (response.body().getMinutely() != null && response.body().getMinutely().size() > 0) {
                minutelyList.clear();
                minutelyList.addAll(response.body().getMinutely());
                mAdapterMinutePrec.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "分钟级降水数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    @Override
    public void getWeatherDataFailed() {
        refresh.finishRefresh();//关闭刷新
        dismissLoadingDialog();//关闭弹窗
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }


    //数据请求失败返回
    @Override
    public void getDataFailed() {
        refresh.finishRefresh();//关闭刷新
        dismissLoadingDialog();//关闭弹窗
        ToastUtils.showShortToast(context, "网络异常");//这里的context是框架中封装好的，等同于this
    }

    @Override
    public void onDestroy() {
        wwBig.stop();//大风车停止转动
        wwSmall.stop();//小风车停止转动
        EventBus.getDefault().unregister(this);//取消注册
        super.onDestroy();
    }
}