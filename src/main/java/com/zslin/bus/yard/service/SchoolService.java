package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassSystemDao;
import com.zslin.bus.yard.dao.ISchoolDao;
import com.zslin.bus.yard.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/10.
 */
@Service
@AdminAuth(name = "学校管理", psn = "科普进校园", orderNum = 1, type = "1", url = "/yard/school")
public class SchoolService {

    @Autowired
    private ISchoolDao schoolDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<School> res = schoolDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 不分页，用于添加老师
     * @param params
     * @return
     */
    public JsonResult listNoPage(String params) {
        List<School> list = schoolDao.findAll();
        return JsonResult.getInstance().set("size", list.size()).set("list", list);
    }

    /**
     * 获取对象
     * @param params {id:0}
     * @return
     */
    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            School s = schoolDao.findOne(id);
            return JsonResult.succ(s);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        School obj = JSONObject.toJavaObject(JSON.parseObject(params), School.class);
        if(obj.getSystemId()!=null && obj.getSystemId()>0) {
            obj.setSystemName(classSystemDao.findOne(obj.getSystemId()).getName());
        }
        if(obj.getId()!=null && obj.getId()>0) { //修改
            School s = schoolDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s, "id");
            schoolDao.save(s);
        } else {
            schoolDao.save(obj);
        }
        return JsonResult.succ(obj);
    }

    /**
     * 修改状态
     * @param params {id:0, status:1}
     * @return
     */
    public JsonResult updateStatus(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String status = JsonTools.getJsonParam(params, "status");
            schoolDao.udpateStatus(id, status);
            return JsonResult.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 删除对象
     * @param params {id:0}
     * @return
     */
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            schoolDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
