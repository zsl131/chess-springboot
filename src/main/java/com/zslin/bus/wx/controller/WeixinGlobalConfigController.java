package com.zslin.bus.wx.controller;

import com.zslin.bus.wx.dao.IWxConfigDao;
import com.zslin.bus.wx.jstools.WxjsConfigDto;
import com.zslin.bus.wx.jstools.WxjsSign;
import com.zslin.bus.wx.model.WxConfig;
import com.zslin.bus.wx.tools.AccessTokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/24 15:09.
 * 微信全局配置
 */
@RestController
@RequestMapping(value = "wxRemote/weixin/globalConfig")
public class WeixinGlobalConfigController {

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private IWxConfigDao wxConfigDao;

    @RequestMapping(value = "config", method = RequestMethod.POST)
    public WxjsConfigDto config(Model model, String url, HttpServletRequest request) {
        if(url.indexOf("#")>=0) {
            url = url.substring(0, url.indexOf("#"));
        }
        Map<String, String> config = WxjsSign.sign(accessTokenTools.getJsTicket(), url);
        WxConfig wc = wxConfigDao.loadOne();
        WxjsConfigDto dto = new WxjsConfigDto(wc.getAppid(), config);
        return dto;
    }
}
