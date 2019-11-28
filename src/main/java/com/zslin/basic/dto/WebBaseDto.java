package com.zslin.basic.dto;

import com.zslin.basic.model.AppConfig;
import com.zslin.bus.wx.model.WxConfig;

/**
 * Created by zsl on 2018/7/24.
 */
public class WebBaseDto {

    private AppConfig ac;

    private WxConfig wc;

    public AppConfig getAc() {
        return ac;
    }

    public void setAc(AppConfig ac) {
        this.ac = ac;
    }

    public WxConfig getWc() {
        return wc;
    }

    public void setWc(WxConfig wc) {
        this.wc = wc;
    }
}
