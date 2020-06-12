package com.zslin.bus.wx.controller;

import com.zslin.bus.wx.tools.SessionTools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2019/5/28.
 */
@Controller
@RequestMapping(value = "wx/test")
public class WxTestController {

    @RequestMapping(value = "index")
    public String index(Model model, String msg, HttpServletRequest request) {
        System.out.println("_______________________>dddd");
        msg = msg==null?"默认信息":msg;
        model.addAttribute("res", msg);
        return "weixin/test/index";
    }

    @GetMapping(value = "openid")
    public @ResponseBody
    String openid(String from, HttpServletRequest request) {
        //如果from为1，则表示是服务器上的正式Openid，否则则是本机的Openid
        String o = "1".equals(from)?"o4Jhl0nkSmiRIqU9JkWIm2lj6qXE":"o4Jhl0nkSmiRIqU9JkWIm2lj6qXE";
        SessionTools.setOpenid(request, o);
        return "ok";
    }
}
