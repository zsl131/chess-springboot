package com.zslin.bus.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxConfigDao;
import com.zslin.bus.wx.model.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/20.
 */
@Service
@AdminAuth(name = "微信配置管理", psn = "微信管理", orderNum = 1, type = "1", url = "/admin/wxConfig")
public class WxConfigService {

    @Autowired
    private IWxConfigDao wxConfigDao;

    public JsonResult loadOne(String params) {
        WxConfig wc = wxConfigDao.loadOne();
        if(wc==null) {wc = new WxConfig();}
        return JsonResult.succ(wc);
    }

    public JsonResult save(String params) {
        WxConfig wc = JSONObject.toJavaObject(JSON.parseObject(params), WxConfig.class);
        wxConfigDao.save(wc);
        return JsonResult.succ(wc);
    }
}
