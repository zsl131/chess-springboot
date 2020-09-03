package com.zslin.bus.yard.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.dto.ClassImageDto;
import com.zslin.bus.yard.dto.PlanCourseDto;
import com.zslin.bus.yard.dto.PlanDto;
import com.zslin.bus.yard.model.*;
import com.zslin.bus.yard.tools.ClassImageTools;
import com.zslin.bus.yard.tools.TeachPlanTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YardIndexService {

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    @Autowired
    private ITeachPlanConfigDao teachPlanConfigDao;

    @Autowired
    private IYardNoticeDao yardNoticeDao;

    @Autowired
    private ITeachPlanDao teachPlanDao;

    @Autowired
    private TeachPlanTools teachPlanTools;

    @Autowired
    private IClassImageDao classImageDao;

    @Autowired
    private ClassImageTools classImageTools;

    public JsonResult index(String params) {
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Teacher tea = teacherDao.findOne(teaId);

//        TeachPlanConfig config = teachPlanConfigDao.loadOne();
        /*List<TeacherClassroom> classroomList = teacherClassroomDao
                .findByTargetYearAndTeaId(config==null? NormalTools.curDate("yyyy"):config.getConfigYear(), teaId);*/
        List<TeacherClassroom> classroomList = teachPlanTools.queryClassroom(teaId);

//        List<PlanDto> planDtoList = teachPlanTools.queryPlan(classroomList);
        //按体系来
        List<PlanCourseDto> courseDtoList = teachPlanTools.queryCourseDto(classroomList);

        //按班级来
        List<PlanCourseDto> roomCourseList = teachPlanTools.queryCourseDto(classroomList, false);

        List<TeachPlan> planList = null;
        List<ClassImageDto> imageList = null;
        try {
            TeacherClassroom classroom = classroomList.get(0);
            planList = teachPlanDao.findByTeacher(teaId, classroom.getTargetYear(), SimpleSortBuilder.generateSort("sid"));

            imageList = classImageTools.build(teaId, classroom.getTargetYear(), classroomList);
        } catch (Exception e) {
        }


        List<YardNotice> noticeList = yardNoticeDao.findShow(SimpleSortBuilder.generateSort("orderNo_a"));
        return JsonResult.success().set("classroomList", classroomList)
                .set("teacher", tea).set("noticeList", noticeList)
                .set("courseDtoList", courseDtoList)
                .set("planList", planList).set("roomCourseList", roomCourseList)
                .set("imageList", imageList);
    }
}
