package com.example.goodweather.utils;

import android.widget.ImageView;

import com.example.goodweather.R;

public class WeatherUtil {

    public static void changeIcon(ImageView weatherStateIcon, int code){
        switch (code){
            case 100://晴
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_100d);
                break;
            case 101://多云
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_101d);
                break;
            case 102://少云
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_102d);
                break;
            case 103://晴间多云
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_103d);
                break;
            case 200://有风
            case 202://微风
            case 203://和风
            case 204://清风
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_200d);//因为这几个状态的图标是一样的
                break;
            case 201://平静
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_201d);
                break;
            case 205://强风/劲风
            case 206://疾风
            case 207://大风
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_205d);//因为这几个状态的图标是一样的
                break;
            case 208://烈风
            case 209://风暴
            case 210://狂爆风
            case 211://飓风
            case 212://龙卷风
            case 213://热带风暴
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_208d);//因为这几个状态的图标是一样的
                break;
            case 300://阵雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_300d);
                break;
            case 301://强阵雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_301d);
                break;
            case 302://雷阵雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_302d);
                break;
            case 303://强雷阵雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_303d);
                break;
            case 304://雷阵雨伴有冰雹
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_304d);
                break;
            case 305://小雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_305d);
                break;
            case 306://中雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_306d);
                break;
            case 307://大雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_307d);
                break;
            case 308://极端降雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_312d);
                break;
            case 309://毛毛雨/细雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_309d);
                break;
            case 310://暴雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_310d);
                break;
            case 311://大暴雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_311d);
                break;
            case 312://特大暴雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_312d);
                break;
            case 313://冻雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_313d);
                break;
            case 314://小到中雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_306d);
                break;
            case 315://中到大雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_307d);
                break;
            case 316://大到暴雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_310d);
                break;
            case 317://大暴雨到特大暴雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_312d);
                break;
            case 399://雨
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_399d);
                break;
            case 400://小雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_400d);
                break;
            case 401://中雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_401d);
                break;
            case 402://大雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_402d);
                break;
            case 403://暴雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_403d);
                break;
            case 404://雨夹雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_404d);
                break;
            case 405://雨雪天气
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_405d);
                break;
            case 406://阵雨夹雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_406d);
                break;
            case 407://阵雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_407d);
                break;
            case 408://小到中雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_408d);
                break;
            case 409://中到大雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_409d);
                break;
            case 410://大到暴雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_410d);
                break;
            case 499://雪
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_499d);
                break;
            case 500://薄雾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_500d);
                break;
            case 501://雾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_501d);
                break;
            case 502://霾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_502d);
                break;
            case 503://扬沙
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_503d);
                break;
            case 504://扬沙
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_504d);
                break;
            case 507://沙尘暴
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_507d);
                break;
            case 508://强沙尘暴
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_508d);
                break;
            case 509://浓雾
            case 510://强浓雾
            case 514://大雾
            case 515://特强浓雾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_509d);
                break;
            case 511://中度霾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_511d);
                break;
            case 512://重度霾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_512d);
                break;
            case 513://严重霾
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_513d);
                break;
            case 900://热
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_900d);
                break;
            case 901://冷
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_901d);
                break;
            case 999://未知
                weatherStateIcon.setBackgroundResource(R.mipmap.icon_999d);
                break;
        }

    }

    /**
     * 根据传入的时间显示时间段描述信息
     * @param timeData
     * @return
     */
    public static String showTimeInfo(String timeData){
        String timeInfo = null;
        int time = 0;
        time = Integer.parseInt(timeData.trim().substring(0,2));
        if (time >= 0 && time <= 6) {
            timeInfo = "凌晨";
        }else if (time > 6 && time <= 12) {
            timeInfo = "上午";
        }else if (time > 12 && time <= 13) {
            timeInfo = "中午";
        }else if (time > 13 && time <= 18) {
            timeInfo = "下午";
        } else if (time > 18 && time <= 24) {
            timeInfo = "晚上";
        } else {
            timeInfo = "未知";
        }
        return timeInfo;
    }

}
