package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppVideoDao;
import com.zslin.bus.app.model.AppVideo;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by zsl on 2018/9/15.
 */
@Service
@AdminAuth(name = "科普视频管理", psn = "系统管理", type = "1", url = "/admin/appVideo", orderNum = 10)
public class AdminAppVideoService {

    @Autowired
    private IAppVideoDao appVideoDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<AppVideo> res = appVideoDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        JsonResult result =  JsonResult.getInstance().set("size", (int) res.getTotalElements())
                .set("data", res.getContent())
                .set("totalPage", res.getTotalPages());

        return result;
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            AppVideo obj = appVideoDao.findOne(id);
            JsonResult res = JsonResult.succ(obj);
            return res;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        try {
            AppVideo obj = JSONObject.toJavaObject(JSON.parseObject(params), AppVideo.class);
            if(obj.getId()==null || obj.getId()<=0) {
                obj.setCreateTime(NormalTools.curDatetime());
                obj.setCreateLong(System.currentTimeMillis());
                obj.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                appVideoDao.save(obj); //添加
            } else {
                AppVideo d = appVideoDao.findOne(obj.getId());
                if(d.getPicPath()!=null && !d.getPicPath().equals(obj.getPicPath())) {
                    //删除原来的图片
                    File file = new File(configTools.getUploadPath() + d.getPicPath());
                    if(file.exists()) {file.delete();}
                }
                MyBeanUtils.copyProperties(obj, d, "id");
                appVideoDao.save(d);
            }
            return JsonResult.succ(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 修改属性值
     * @param params {id: 0, field:'status', value: '0'}
     * @return
     */
    public JsonResult updateField(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String field = JsonTools.getJsonParam(params, "field");
            String value = JsonTools.getJsonParam(params, "value");
            appVideoDao.updateByHql("UPDATE AppVideo n SET n."+field+"=?1 WHERE n.id="+id, value);
            return JsonResult.success("设置成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            appVideoDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
