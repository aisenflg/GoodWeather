package com.example.goodweather.api;

import com.example.goodweather.bean.AirNowResponse;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.DailyResponse;
import com.example.goodweather.bean.HotCityBean;
import com.example.goodweather.bean.HourlyResponse;
import com.example.goodweather.bean.LifestyleResponse;
import com.example.goodweather.bean.NewSearchCityResponse;
import com.example.goodweather.bean.NowResponse;
import com.example.goodweather.bean.SearchCityBean;
import com.example.goodweather.bean.WeatherBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 当天天气查询
     * https://free-api.heweather.net/s6/weather/now?key=756dc8a018744a75a6200810559528a9&location=深圳
     *   将地址进一步拆分，将可变的一部分放在注解@GET的地址里面，其中
     *   /s6/weather/now?key=756dc8a018744a75a6200810559528a9 这一部分在这个接口中又是不变的，变的是location的值
     *   所以将location的参数放入@Query里面，因为是使用的GET请求，所以里面的内容会拼接到地址后面，并且自动会加上 & 符号
     *   Call是retrofit2框架里面的，这个框架是对OKHttp的进一步封装，会让你的使用更加简洁明了，里面放入之前通过接口返回
     *   的JSON字符串生成返回数据实体Bean,Retrofit支持Gson解析实体类,所以，后面的返回值就不用做解析了。
     *   getTodayWeather是这个接口的方法名。这样说应该很清楚了吧
     * @param location  区/县
     * @return
     */


    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BiYingImgBean> biying();


    /**
     * 天气预报数据
     * @param location
     * @return
     */
    @GET("/s6/weather?key=756dc8a018744a75a6200810559528a9")
    Call<WeatherBean> getWeatherData(@Query("location") String location);

    /**
     * 搜索城市
     * @param location
     * @return
     */
    @GET("/find?key=3086e91d66c04ce588a7f538f917c7f4&group=cn&number=10")
    Call<SearchCityBean> searchCity(@Query("location") String location);

    /**
     * 海外热门城市
     */
    @GET("/top?key=756dc8a018744a75a6200810559528a9&number=50&lang=zh")
    Call<HotCityBean> hotCity(@Query("group") String group);

    /**
     * 实况天气
     * @param location 城市名
     * @return 返回实况天气数据
     */
    @GET("/v7/weather/now?key=756dc8a018744a75a6200810559528a9")
    Call<NowResponse> nowWeather(@Query("location") String location);

    /**
     * 天气预报  因为是开发者所以最多可以获得15天的数据，但是如果你是普通用户，那么最多只能获得三天的数据
     * 分为 3天、7天、10天、15天 四种情况，这是时候就需要动态的改变请求的url
     * @param type  天数类型  传入3d / 7d / 10d / 15d  通过Path拼接到请求的url里面
     * @param location 城市名
     * @return 返回天气预报数据
     */
    @GET("/v7/weather/{type}?key=756dc8a018744a75a6200810559528a9")
    Call<DailyResponse> dailyWeather(@Path("type") String type, @Query("location") String location);


    /**
     * 逐小时预报（未来24小时）之前是逐三小时预报
     * @param location  城市名
     * @return 返回逐小时数据
     */
    @GET("/v7/weather/24h?key=756dc8a018744a75a6200810559528a9")
    Call<HourlyResponse> hourlyWeather(@Query("location") String location);

    /**
     * 当天空气质量
     * @param location 城市名
     * @return 返回当天空气质量数据
     */
    @GET("/v7/air/now?key=756dc8a018744a75a6200810559528a9")
    Call<AirNowResponse> airNowWeather(@Query("location") String location);


    /**
     * 生活指数
     * @param type 可以控制定向获取那几项数据 全部数据 0, 运动指数	1 ，洗车指数	2 ，穿衣指数	3 ，
     *             钓鱼指数	4 ，紫外线指数  5 ，旅游指数  6，花粉过敏指数	7，舒适度指数	8，
     *             感冒指数	9 ，空气污染扩散条件指数	10 ，空调开启指数	 11 ，太阳镜指数	12 ，
     *             化妆指数  13 ，晾晒指数  14 ，交通指数  15 ，防晒指数	16
     * @param location 城市名
     * @return 返回当前生活指数数据
     * @return
     */
    @GET("/v7/indices/1d?key=756dc8a018744a75a6200810559528a9")
    Call<LifestyleResponse> lifestyle(@Query("type") String type,
                                      @Query("location") String location);


    /**
     * 搜索城市  V7版本  模糊搜索，国内范围 返回10条数据
     *
     * @param location 城市名
     * @param mode     exact 精准搜索  fuzzy 模糊搜索
     * @return
     */
    @GET("/v2/city/lookup?key=756dc8a018744a75a6200810559528a9&range=cn")
    Call<NewSearchCityResponse> newSearchCity(@Query("location") String location,
                                              @Query("mode") String mode);


}
