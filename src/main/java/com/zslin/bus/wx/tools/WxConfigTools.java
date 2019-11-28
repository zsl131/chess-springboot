package com.zslin.bus.wx.tools;

import com.zslin.bus.wx.dao.IWxConfigDao;
import com.zslin.bus.wx.model.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/7/20.
 */
@Component
public class WxConfigTools {

    @Autowired
    private IWxConfigDao wxConfigDao;

    private static WxConfig wxConfig;

    public WxConfig getWxConfig() {
        if(wxConfig == null) {
            wxConfig = wxConfigDao.loadOne();
        }
        return wxConfig;
    }
}
