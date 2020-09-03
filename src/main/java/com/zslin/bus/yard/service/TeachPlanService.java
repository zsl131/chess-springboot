package com.zslin.bus.yard.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.model.*;
import com.zslin.bus.yard.tools.TeachPlanConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AdminAuth(name = "教案管理", psn = "科普进校园", orderNum = 1, type = "1", url = "/yard/teachPlan")
public class TeachPlanService {

    @Autowired
    private ITeachPlanDao teachPlanDao;

    @Autowired
    private ITeachPlanConfigDao teachPlanConfigDao;

    @Autowired
    private TeachPlanConfigTools teachPlanConfigTools;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    /** 查找对应课程的教案 */
    public JsonResult queryPlan(String params) {
        String year = teachPlanConfigTools.getCurYear(); //年份
        Integer courseId = JsonTools.getIntegerParams(params, "courseId"); //课程ID
        Integer teaId = JsonTools.getIntegerParams(params, "teaId"); //教师ID
        TeachPlan plan = teachPlanDao.findOne(teaId, year, courseId);
        //flag：获取到数据则为true，数据为null则为false
        return JsonResult.success("获取成功").set("plan", plan).set("course", classCourseDao.findOne(courseId))
                .set("flag", plan!=null);
    }

    /**
     * 添加工修改教案
     * @param params
     * @return
     */
    public JsonResult addOrUpdatePlan(String params) {
        String year = teachPlanConfigTools.getCurYear(); //年份
        Integer courseId = JsonTools.getIntegerParams(params, "courseId"); //课程ID
        Integer teaId = JsonTools.getIntegerParams(params, "teaId"); //教师ID
        TeachPlan plan = teachPlanDao.findOne(teaId, year, courseId);

        String guide = JsonTools.getJsonParam(params, "guide"); //课程导入
        String teachStep = JsonTools.getJsonParam(params, "teachStep"); //授课过程
        String nextTeach = JsonTools.getJsonParam(params, "nextTeach"); //过渡下一课程
        if(plan==null) {
            plan = new TeachPlan();
            ClassCourse course = classCourseDao.findOne(courseId);
            TeacherClassroom classroom = queryClassroom(courseId, year, teaId);

            Teacher teacher = teacherDao.findOne(teaId);
            TeachPlanConfig config = teachPlanConfigDao.loadOne();

            plan.setCourseId(course.getId());
            plan.setCourseTitle(course.getTitle());
            /*plan.setGradeId(course.getGradeId());
            plan.setGradeName(course.getGrade());*/
            if(classroom!=null) {
                plan.setGradeId(classroom.getGradeId());
                plan.setGradeName(classroom.getGradeName());
                plan.setSid(classroom.getSid());
                plan.setSname(classroom.getSname());
            }

            plan.setKeyPoint(course.getKeyPoint());

            if(config!=null) {
                plan.setPlanTerm(config.getTerm());
            }
            plan.setPlanYear(year);
            plan.setSchId(teacher.getSchoolId());
            plan.setSchName(teacher.getSchoolName());
//            plan.setSid(course.get);
            plan.setTeaId(teacher.getId());
            plan.setTeaName(teacher.getName());
            plan.setTeaPhone(teacher.getPhone());
        }
        plan.setGuide(guide);
        plan.setTeachStep(teachStep);
        plan.setNextTeach(nextTeach);
        teachPlanDao.save(plan);
        return JsonResult.success("保存成功").set("plan", plan);
    }

    private TeacherClassroom queryClassroom(Integer courseId, String year, Integer teaId) {
        List<TeacherClassroom> list = teacherClassroomDao.queryByCourseId(courseId, year, teaId);
        if(list!=null && list.size()>0) {return list.get(0);}
        return null;
    }

    public JsonResult queryClassroom(String params) {
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        String year = teachPlanConfigTools.getCurYear();
        List<TeacherClassroom> list = teacherClassroomDao.queryByCourseId(courseId, year, teaId);
        return JsonResult.success().set("classroomList", list);
    }
}
