package com.zslin.bus.sms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.sms.dao.ISmsConfigDao;
import com.zslin.bus.sms.model.SmsConfig;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.tools.SmsConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/10/9.
 */
@Service
@AdminAuth(name = "短信配置管理", psn = "短信管理", url = "/admin/smsConfig", type = "1", orderNum = 1)
public class SmsConfigService {

    @Autowired
    private ISmsConfigDao smsConfigDao;

    @Autowired
    private SmsConfigTools smsConfigTools;

    public JsonResult loadOne(String params) {
        SmsConfig wc = smsConfigDao.loadOne();
        if(wc==null) {wc = new SmsConfig();}
        return JsonResult.succ(wc);
    }

    public JsonResult save(String params) {
        SmsConfig wc = JSONObject.toJavaObject(JSON.parseObject(params), SmsConfig.class);
        SmsConfig wcOld = smsConfigDao.loadOne();
        if(wcOld==null) {
            smsConfigDao.save(wc);
            smsConfigTools.setSmsConfig(wc);
            return JsonResult.succ(wc);
        } else {
            MyBeanUtils.copyProperties(wc, wcOld, "id");
            smsConfigDao.save(wcOld);
            smsConfigTools.setSmsConfig(wcOld);
            return JsonResult.succ(wcOld);
        }
    }
}
