package com.zslin.bus.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppVersionDao;
import com.zslin.bus.app.model.AppVersion;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2019/10/1.
 */
@Service
@AdminAuth(name = "更新管理", psn = "移动端管理", url = "/admin/appVersion", type = "1", orderNum = 4)
public class AppVersionService {

    @Autowired
    private IAppVersionDao appVersionDao;

    /**
     * 检测更新
     * @param params
     * @return
     */
    public JsonResult checkUpdate(String params) {
        //System.out.println("=========>"+params);
        String appid = JsonTools.getJsonParam(params, "appid");
        String version = JsonTools.getJsonParam(params, "version");
        AppVersion av = appVersionDao.loadOne();
        if(av==null) {
            av = new AppVersion();
        }
        if(av.getStatus()!=null && "1".equals(av.getStatus()) && version!=null && av.getVersion()!=null && !version.equals(av.getVersion())) {
            av.setStatus("1");
        } else {
            av.setStatus("0");
        }
        return JsonResult.succ(av);
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        AppVersion a = appVersionDao.findOne(id);
        return JsonResult.succ(a);
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<AppVersion> list = appVersionDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", list.getTotalElements()).set("data", list.getContent());
    }

    public JsonResult addOrUpdate(String params) {
        AppVersion obj = JSONObject.toJavaObject(JSON.parseObject(params), AppVersion.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            AppVersion s = appVersionDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s, "id", "status", "flag", "isForce");
            appVersionDao.save(s);
        } else {
            obj.setCreateLong(System.currentTimeMillis());
            obj.setCreateTime(NormalTools.curDatetime());
            obj.setCreateDay(NormalTools.curDate());
            appVersionDao.save(obj);
        }
        return JsonResult.succ(obj);
    }

    public JsonResult updateField(String params) {
        try {
            String field = JsonTools.getJsonParam(params, "field");
            String val = JsonTools.getJsonParam(params, "val");
            Integer id = JsonTools.getId(params);
            if("status".equalsIgnoreCase(field)) {
                appVersionDao.updateStatus(val, id);
            } else if("flag".equalsIgnoreCase(field)) {
                appVersionDao.updateFlag(val, id);
            } else if("isForce".equalsIgnoreCase(field)) {
                appVersionDao.updateForce(val, id);
            }
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
