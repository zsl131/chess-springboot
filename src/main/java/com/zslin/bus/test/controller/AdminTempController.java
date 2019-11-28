package com.zslin.bus.test.controller;

import com.zslin.bus.wx.tools.ExchangeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/10/5.
 */
@RestController
@RequestMapping(value = "test/temp")
public class AdminTempController {

    @Autowired
    private ExchangeTools exchangeTools;

    @GetMapping(value = "test")
    public String test(HttpServletRequest request) {
        String str = request.getParameter("str");
        return "测试请求："+str;
    }

    @GetMapping(value = "listWxUsers")
    public String listWxUsers(HttpServletRequest request) {
        exchangeTools.listWxUsers(null);
        return "处理完成";
    }
}
