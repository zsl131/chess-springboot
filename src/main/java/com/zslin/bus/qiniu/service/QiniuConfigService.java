package com.zslin.bus.qiniu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.qiniu.dao.IQiniuConfigDao;
import com.zslin.bus.qiniu.model.QiniuConfig;
import com.zslin.bus.qiniu.tools.QiniuConfigTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/12/1.
 */
@Service
@AdminAuth(name = "七牛配置管理", psn = "七牛管理", url = "/admin/qiniuConfig", type = "1", orderNum = 1)
public class QiniuConfigService {

    @Autowired
    private IQiniuConfigDao qiniuConfigDao;

    @Autowired
    private QiniuConfigTools qiniuConfigTools;

    public JsonResult loadOne(String params) {
        QiniuConfig wc = qiniuConfigDao.loadOne();
        if(wc==null) {wc = new QiniuConfig();}
        return JsonResult.succ(wc);
    }

    public JsonResult save(String params) {
        QiniuConfig wc = JSONObject.toJavaObject(JSON.parseObject(params), QiniuConfig.class);
        QiniuConfig wcOld = qiniuConfigDao.loadOne();
        if(wcOld==null) {
            qiniuConfigDao.save(wc);
            qiniuConfigTools.setConfig(wc);
            return JsonResult.succ(wc);
        } else {
            MyBeanUtils.copyProperties(wc, wcOld);
            qiniuConfigDao.save(wcOld);
            qiniuConfigTools.setConfig(wcOld);
            return JsonResult.succ(wcOld);
        }
    }
}
