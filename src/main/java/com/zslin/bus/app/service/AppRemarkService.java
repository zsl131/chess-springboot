package com.zslin.bus.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.app.dao.IAppRemarkDao;
import com.zslin.bus.app.model.AppRemark;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2019/9/25.
 */
@Service
@AdminAuth(name = "说明管理", psn = "移动端管理", url = "/admin/appRemark", type = "1", orderNum = 4)
public class AppRemarkService {

    @Autowired
    private IAppRemarkDao appRemarkDao;

    public JsonResult loadOne(String params) {
        AppRemark remark = appRemarkDao.loadOne();
        return JsonResult.succ(remark);
    }

    public JsonResult addOrUpdate(String params) {
        AppRemark obj = JSONObject.toJavaObject(JSON.parseObject(params), AppRemark.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            AppRemark s = appRemarkDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s, "id");
            appRemarkDao.save(s);
        } else {
            appRemarkDao.save(obj);
        }
        return JsonResult.succ(obj);
    }
}
