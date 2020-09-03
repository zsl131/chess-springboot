package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.ITeachPlanConfigDao;
import com.zslin.bus.yard.model.TeachPlanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AdminAuth(name = "教案配置管理", psn = "科普进校园", orderNum = 1, type = "1", url = "/yard/teachPlanConfig")
public class TeachPlanConfigService {

    @Autowired
    private ITeachPlanConfigDao teachPlanConfigDao;

    public JsonResult loadOne(String params) {
        TeachPlanConfig tpc = teachPlanConfigDao.loadOne();
        if(tpc==null) {tpc = new TeachPlanConfig();}
        return JsonResult.succ(tpc);
    }

    public JsonResult save(String params) {
        TeachPlanConfig tpc = JSONObject.toJavaObject(JSON.parseObject(params), TeachPlanConfig.class);
        TeachPlanConfig wcOld = teachPlanConfigDao.loadOne();
        if(wcOld==null) {
            teachPlanConfigDao.save(tpc);
            return JsonResult.succ(tpc);
        } else {
            MyBeanUtils.copyProperties(tpc, wcOld);
            teachPlanConfigDao.save(wcOld);
            return JsonResult.succ(wcOld);
        }
    }
}
