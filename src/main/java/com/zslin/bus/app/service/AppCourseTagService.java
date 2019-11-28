package com.zslin.bus.app.service;

import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassTagDao;
import com.zslin.bus.yard.model.ClassTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppCourseTagService {

    @Autowired
    private IClassTagDao classTagDao;

    /** 获取所有标签 */
    public JsonResult listAllTag(String params) {
        List<ClassTag> list = classTagDao.findAll();
        return JsonResult.succ(list);
    }
}
