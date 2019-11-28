package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.basic.dao.INoticeCategoryDao;
import com.zslin.bus.basic.model.NoticeCategory;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/20.
 */
@Service
public class NoticeCategoryService {

    @Autowired
    private INoticeCategoryDao noticeCategoryDao;

    public JsonResult list(String params) {
        List<NoticeCategory> list = noticeCategoryDao.findAll();
        return JsonResult.getInstance().set("size", list.size()).set("data", list);
    }

    public JsonResult addOrUpdate(String params) {
        try {
            NoticeCategory obj = JSONObject.toJavaObject(JSON.parseObject(params), NoticeCategory.class);
            if(obj.getId()!=null && obj.getId()>0) { //修改
                NoticeCategory nc = noticeCategoryDao.findOne(obj.getId());
                MyBeanUtils.copyProperties(obj, nc, "id");
                noticeCategoryDao.save(nc);
                return JsonResult.succ(nc);
            } else {
                noticeCategoryDao.save(obj);
                return JsonResult.succ(obj);
            }
//            return JsonResult.success("保存成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            return JsonResult.succ(noticeCategoryDao.findOne(id));
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }


    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            noticeCategoryDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
