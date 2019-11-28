package com.zslin.bus.qrcode.controller;

import com.zslin.bus.qrcode.dao.IQrcodeDao;
import com.zslin.bus.qrcode.model.Qrcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zsl on 2019/3/3.
 */
@RestController
@RequestMapping(value = "api/qr")
public class QrController {

    @Autowired
    private IQrcodeDao qrcodeDao;

    @RequestMapping(value = {"", "/"})
    public String index(HttpServletRequest request, Integer id, HttpServletResponse response) {
        try {
            Qrcode code = qrcodeDao.findOne(id);
            if(code==null) {
                return "Not found config by : "+id;
            } else if(!"1".equals(code.getStatus())) {
                return "Stopped ID : "+id;
            } else {
                response.sendRedirect(code.getUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirecting...";
    }
}
