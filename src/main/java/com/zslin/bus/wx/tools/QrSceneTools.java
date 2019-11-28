package com.zslin.bus.wx.tools;

import com.zslin.bus.wx.dao.IWxLoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/9/25.
 */
@Component
public class QrSceneTools {

    @Autowired
    private IWxLoginDao wxLoginDao;

    /**
     * 处理微信扫码
     * @param openid 扫码者Openid
     * @param text 接收到的Text
     */
    public void onScene(String openid, String text) {
        if(text==null || "".equals(text)) return;
        if(text.startsWith("lo_") || text.startsWith("bind_")) { //扫码登陆、扫码绑定
            wxLoginDao.updateOpenid(openid, text);
        }
    }
}
