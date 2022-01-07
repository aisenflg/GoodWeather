package com.example.mvplibrary.bean;

import org.litepal.crud.LitePalSupport;

public class WallPaper extends LitePalSupport {
    private String ImgUrl;

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

}
