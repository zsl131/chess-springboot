package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.ITeacherVideoCountDao;
import com.zslin.bus.yard.model.TeacherVideoCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2019/2/12.
 */
@Service
public class TeacherVideoCountService {

    @Autowired
    private ITeacherVideoCountDao teacherVideoCountDao;

    public JsonResult updateCount(String params) {
        TeacherVideoCount obj = JSONObject.toJavaObject(JSON.parseObject(params), TeacherVideoCount.class);
        TeacherVideoCount tvc = teacherVideoCountDao.findByUsernameAndCourseId(obj.getUsername(), obj.getCourseId());
        if(tvc==null) {
            teacherVideoCountDao.save(obj);
        } else {
            tvc.setCount(tvc.getCount() + obj.getCount());
            teacherVideoCountDao.save(tvc);
        }
        return JsonResult.success("保存成功");
    }
}
