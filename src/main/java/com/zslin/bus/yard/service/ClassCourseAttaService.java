package com.zslin.bus.yard.service;

import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCourseAttaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassCourseAttaService {

    @Autowired
    private IClassCourseAttaDao classCourseAttaDao;

    public JsonResult delete(String params) {
        try {
            Integer id = JsonTools.getId(params);
            classCourseAttaDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            return JsonResult.success("删除成功");
        }
    }
}
