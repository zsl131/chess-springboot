package com.zslin.bus.basic.service;


import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.basic.dao.ICourseDao;
import com.zslin.bus.basic.model.Course;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AdminAuth(name = "课程选课管理", psn = "系统管理", url = "/admin/course", type = "1", orderNum = 6)
public class CourseService {
    @Autowired
    private ICourseDao courseDao;

    public JsonResult list(String params) {
        try {
            QueryListDto qld = QueryTools.buildQueryListDto(params);
            Page<Course> res = courseDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                    SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
            return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
       Course a = JSON.toJavaObject(JSON.parseObject(params),Course.class);
       if(a.getId()!=null && a.getId()>=0) {
           Course c = courseDao.findOne(a.getId());
           c.setCategory(a.getCategory());
           if(a.getCategory().equals("生命科学")) {
               a.setA("周五18:40-19:40");
               a.setB("周六18:40-19:40");
               a.setC("暂无课程安排");
               a.setD("暂无课程安排");
               courseDao.save(a);
               return JsonResult.success("修改成功");
           } else {
               c.setOptions(a.getOptions());
               courseDao.save(c);
               return JsonResult.success("修改成功");
           }
       } else if(a.getCategory().equals("生命科学")) {
           a.setA("周五18:40-19:40");
           a.setB("周六18:40-19:40");
           a.setC("暂无课程安排");
           a.setD("暂无课程安排");
           courseDao.save(a);
           return JsonResult.success("修改成功");
       } else {
           courseDao.save(a);
           return JsonResult.success("保存成功");
       }
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
            Course obj = courseDao.findOne(id);
            return JsonResult.succ(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
    public JsonResult delete(String params) {
        Integer id  = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
        courseDao.delete(id);
        return JsonResult.success("删除成功");
    }
}
