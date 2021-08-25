package com.example.goodweather.api;

import com.example.goodweather.bean.AirNowCityBean;
import com.example.goodweather.bean.BiYingImgBean;
import com.example.goodweather.bean.HotCityBean;
import com.example.goodweather.bean.SearchCityBean;
import com.example.goodweather.bean.WeatherBean;

import retrofit2.Call;
import retrofit2.http.GET;
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
     * 空气质量数据,这个location要传入市的参数,不再是区/县,否则会提示permission denied 无权限访问
     * @param location
     * @return
     */
    @GET("/s6/air/now?key=756dc8a018744a75a6200810559528a9")
    Call<AirNowCityBean> getAirNowCity(@Query("location") String location);

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
    @GET("/top?key=756dc8a018744a75a6200810559528a9&group=overseas&number=50&lang=zh")
    Call<HotCityBean> hotCity();

}
