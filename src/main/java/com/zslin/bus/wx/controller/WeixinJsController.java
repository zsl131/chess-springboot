package com.zslin.bus.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxConfigDao;
import com.zslin.bus.wx.model.WxConfig;
import com.zslin.bus.wx.tools.InternetTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

/**
 * Created by zsl on 2019/9/3.
 */
@Controller
@RequestMapping(value = "weixin/js")
public class WeixinJsController {

    @Autowired
    private IWxConfigDao wxConfigDao;

    @ResponseBody
    @RequestMapping(value = "getTicket", method = RequestMethod.POST)
    public JsonResult getTicket(String url) {
        WxConfig config = wxConfigDao.loadOne();
        String jsapi_ticket = getJsapiTicket(config);
        String  noncestr =  getNonceStr();
        String timestamp =  getTimeStamp();
        String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonResult result = JsonResult.getInstance();
        result.set("appid", config.getAppid())
                .set("noncestr", noncestr)
                .set("timestamp", timestamp)
                .set("sign", signature)
                .set("ticket", jsapi_ticket);
        return result;
    }

    private String getJsapiTicket(WxConfig config){

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?";
        String params = "grant_type=client_credential&appid=" + config.getAppid() + "&secret=" + config.getSecret() + "";
        String result = InternetTools.doGet(requestUrl+params, null);
        //System.out.println("-----WeixinJsController--------"+result);
        String access_token = JSONObject.parseObject(result).getString("access_token");
        requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?";
        params = "access_token=" + access_token + "&type=jsapi";
        result =  InternetTools.doGet(requestUrl+params, null);
        //System.out.println("-----WeixinJsController++++++++++"+result);
        String jsapi_ticket = JSONObject.parseObject(result).getString("ticket");
        int activeTime=Integer.parseInt(JSONObject.parseObject(result).getString("expires_in"));
//        Jssdk jssdk = new Jssdk();
//        jssdk.setActiveTime(activeTime-600);
//        jssdk.setJsapiTicket(jsapi_ticket);
//        jssdkDao.update(jssdk);
        return jsapi_ticket;
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    private  String getNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private  String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);

    }
}
