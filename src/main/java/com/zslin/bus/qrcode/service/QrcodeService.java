package com.zslin.bus.qrcode.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.qrcode.dao.IQrcodeDao;
import com.zslin.bus.qrcode.dao.IQrconfigDao;
import com.zslin.bus.qrcode.model.Qrcode;
import com.zslin.bus.qrcode.model.Qrconfig;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2019/3/3.
 */
@Service
@AdminAuth(name = "二维码管理", psn = "系统管理", orderNum = 3, type = "1", url = "/admin/qrcode")
public class QrcodeService {

    @Autowired
    private IQrcodeDao qrcodeDao;

    @Autowired
    private IQrconfigDao qrconfigDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Qrcode> res = qrcodeDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent()).set("config", qrconfigDao.loadOne());
    }

    public JsonResult loadConfig(String params) {
        Qrconfig config = qrconfigDao.loadOne();
        return JsonResult.succ(config);
    }

    public JsonResult saveConfig(String params) {
        Qrconfig config = JSONObject.toJavaObject(JSON.parseObject(params), Qrconfig.class);
        Qrconfig c = qrconfigDao.loadOne();
        if(c==null) {qrconfigDao.save(config);}
        else {
            MyBeanUtils.copyProperties(config, c);
            qrconfigDao.save(c);
        }
        return JsonResult.success("保存成功");
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        Qrcode code = qrcodeDao.findOne(id);
        return JsonResult.succ(code);
    }

    public JsonResult addOrUpdateCode(String params) {
        Qrcode code = JSONObject.toJavaObject(JSON.parseObject(params), Qrcode.class);
        if(code.getId()!=null && code.getId()>0) { //修改
            Qrcode c = qrcodeDao.findOne(code.getId());
            MyBeanUtils.copyProperties(code, c);
            qrcodeDao.save(c);
        } else {
            qrcodeDao.save(code);
        }
        return JsonResult.success("保存成功");
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            qrcodeDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
