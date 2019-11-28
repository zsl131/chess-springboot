package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.basic.dao.ISelectLessonDao;
import com.zslin.bus.basic.model.SelectLesson;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.jar.JarEntry;

@Service
@AdminAuth(name="选课管理",psn="系统管理",orderNum = 8, type = "1", url = "/admin/selectLesson")
public class SelectLessonService {
    @Autowired
    private ISelectLessonDao selectLessonDao;

    public JsonResult list(String params){
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<SelectLesson> res = selectLessonDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), JsonParamTools.buildAuthDepSpe(params)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
    }
    public JsonResult addOrUpdate(String params){
       SelectLesson s = JSON.toJavaObject(JSON.parseObject(params),SelectLesson.class);
       if(s.getId()<=0||s.getId()==null){
           selectLessonDao.save(s);
       }else{
           SelectLesson old = selectLessonDao.findOne(s.getId());
           old.setLesson(s.getLesson());
           old.setCreateDate(s.getCreateDate());
           old.setCreateTime(s.getCreateTime());
           old.setStatus(s.getStatus());
           selectLessonDao.save(old);
           return JsonResult.success("修改成功");
       }
       return JsonResult.succ(s);
    }
    public JsonResult delete(String params){
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
        selectLessonDao.delete(id);
        return JsonResult.success("删除成功");
    }
}
