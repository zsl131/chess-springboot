package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.ITeachPlanDao;
import com.zslin.bus.yard.model.TeachPlan;
import com.zslin.bus.yard.tools.TeachPlanConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppTeachPlanService {

    @Autowired
    private ITeachPlanDao teachPlanDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private TeachPlanConfigTools teachPlanConfigTools;

    public JsonResult listByCourse(String params) {
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        Integer teaId = JsonTools.getUserId(params);
        String year = teachPlanConfigTools.getCurYear();

        List<TeachPlan> planList = teachPlanDao.findByTeacher(teaId, courseId, year,
                SimpleSortBuilder.generateSort("orderNo_a"));

        return JsonResult.success().set("planList", planList)
                .set("course", classCourseDao.findOne(courseId))
                .set("year", year);
    }
}
