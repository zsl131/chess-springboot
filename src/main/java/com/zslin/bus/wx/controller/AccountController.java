package com.zslin.bus.wx.controller;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.tools.Base64Utils;
import com.zslin.bus.tools.JsonObj;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.model.WxAccount;
import com.zslin.bus.wx.tools.ExchangeTools;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by zsl on 2018/7/27.
 */
@RestController
@RequestMapping(value = "wxRemote")
public class AccountController {

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private ExchangeTools exchangeTools;

    @GetMapping(value = "queryAccountTemp")
    public String queryAccountTemp(String from, HttpServletRequest request) {
        String openid = "0".equals(from)?"orLIDuFuyeOygL0FBIuEilwCF1lU":"orLIDuFuyeOygL0FBIuEilwCF1lU";
        WxAccount account = wxAccountDao.findByOpenid(openid);
        String str = JSON.toJSONString(account);
//        System.out.println("str::"+str);
        request.getSession().setAttribute("wxLoginAccount", str);
        return str;
    }

    @GetMapping(value = "queryAccount")
    public void queryAccount(String targetUrl, HttpServletRequest request, HttpServletResponse response) {

        try {
            String code = request.getParameter("code");
            String state = request.getParameter("state");  //正确值：hztOauth2，参考授权跳转地址
//            System.out.println("code::"+code+", url::"+targetUrl+",state::"+state);
            String realUrl = URLDecoder.decode(Base64Utils.getFromBase64(targetUrl), "UTF-8");
//            System.out.println("realUrl::"+realUrl);
            if(code!=null && !"".equals(code) && state!=null && state.equals("hztOauth2")) { //如果code存在
                String openid = exchangeTools.getUserOpenId(code); //获取openid
                WxAccount account = wxAccountDao.findByOpenid(openid);
                JsonObj jsonObj = new JsonObj(account == null ? 0:1, account);

                String str = JSON.toJSONString(jsonObj);
//                str = new BASE64Encoder().encode(str.getBytes("UTF-8"));
                str = Base64.encodeBase64String(str.getBytes("GBK"));
//                System.out.println("accountBase64:::"+str);

                realUrl += (realUrl.indexOf("?")>=0)?"&":"?";

//                Base64.encodeBase64(str.getBytes("GBK"))

//                realUrl += "account="+str+"&targetUrl="+new BASE64Encoder().encode(realUrl.getBytes("UTF-8"));
                realUrl += "account="+str+"&targetUrl="+Base64.encodeBase64String(realUrl.getBytes("GBK"));

//                System.out.println("AccountController::"+realUrl);

                response.sendRedirect(realUrl);

            } else {
                String wxUrl = rebuildUrl(realUrl) + "?targetUrl="+targetUrl;
//                String realPath = fullPath(request);
//                System.out.println("wxUrl:::"+wxUrl);
                String fullPath = URLEncoder.encode(wxUrl, "UTF-8");
                String appId = exchangeTools.getWxConfig().getAppid();
                String authPath = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=hztOauth2#wechat_redirect";
                authPath = authPath.replace("APPID", appId).replace("REDIRECT_URI", fullPath);
                response.sendRedirect(authPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String rebuildUrl(String url) {
        String tempStr = "_temp_str_";
        String tempUrl = url.replace("://", tempStr);
        if(tempUrl.indexOf("/")<0) {
            return tempUrl.replace(tempStr, "://") + "/wxRemote/queryAccount";
        } else {
            return tempUrl.substring(0, tempUrl.indexOf("/")).replace(tempStr, "://") + "/wxRemote/queryAccount";
        }
    }

    /** 获取全路径 */
    private String fullPath(HttpServletRequest request) {
        String param = request.getQueryString()==null?"":request.getQueryString();
        String res = request.getRequestURL().toString()+"?" + param;
        return res;
    }
}
