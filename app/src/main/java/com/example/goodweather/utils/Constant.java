package com.example.goodweather.utils;

/**
 * 统一管理缓存中对应的KEY
 */
public class Constant {
    /**
     * 壁纸地址
     */
    public static final String WALLPAPER_URL = "wallpaperUrl";
    /**
     * 壁纸类型  1  壁纸列表  2  每日一图  3  手动上传  4  默认壁纸
     */
    public static final String WALLPAPER_TYPE = "wallpaperType";
    //网络壁纸请求成功
    public static final String SUCCESS = "success";

    public final static String SUCCESS_CODE = "200";
    public final static String CITY = "city";//市
    public final static String DISTRICT = "district";//区/县
    public final static String EVERYDAY_IMG = "everyday_img";//每日图片开关
    public final static String IMG_LIST = "img_list";//图片列表开关
    public final static String CUSTOM_IMG = "custom_img";//手动定义开关
    public final static String IMG_POSITION = "img_position";//选中的本地图片背景
    public final static int SELECT_PHOTO = 2;//启动相册表示
    public final static String CUSTOM_IMG_PATH = "custom_img_path";//手动上传图片地址
    public final static String FLAG_OTHER_RETURN="flag_other_return";//跳转页面的标识
    public final static String LOCATION="location";
    public final static String APIKEY="0162564610bf71b62e1c4f3a9782609a";
    public final static String APPKEY="c974e9943f9d28dc642a626eec09f69a";
    public final static String APP_FIRST_START = "appFirstStart";//App首次启动
    public final static String START_UP_APP_TIME = "startAppTime";//今日启动APP的时间
    public final static String ALL_RECORD = "all";//今日启动APP的时间


    /**
     * 每日提示弹窗的背景图
     */
    public static final String EVERYDAY_TIP_IMG = "everydayTipImg";

    /**
     * 每日提示弹窗是否弹出
     */
    public static final String EVERYDAY_POP_BOOLEAN = "everydayPopBoolean";

}
