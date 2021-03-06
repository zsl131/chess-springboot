package com.zslin.bus.yard.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.model.*;
import com.zslin.bus.yard.tools.TeachPlanConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private ISchoolDao schoolDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    @Autowired
    private ITeachPlanFlagDao teachPlanFlagDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<TeachPlan> res = teachPlanDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    /** 查找对应课程的教案 */
    public JsonResult queryPlan(String params) {
        String year = teachPlanConfigTools.getCurYear(); //年份
        Integer courseId = JsonTools.getIntegerParams(params, "courseId"); //课程ID
        Integer teaId = JsonTools.getIntegerParams(params, "teaId"); //教师ID
        TeachPlanFlag tpf = teachPlanFlagDao.queryOne(teaId, courseId, year);
        List<TeachPlan> planList = teachPlanDao.findByTeacher(teaId, courseId, year,
                SimpleSortBuilder.generateSort("orderNo_a"));
        //flag：获取到数据则为true，数据为null则为false
        return JsonResult.success("获取成功").set("planList", planList).set("course", classCourseDao.findOne(courseId))
                .set("flag", (planList!=null&&planList.size()>0)).set("planFlag", tpf);
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
        Integer id = JsonTools.getId(params);
        TeachPlan plan = null;
        if(id>0) {plan = teachPlanDao.findOne(id);} //如果有ID

//        TeachPlan plan = teachPlanDao.findOne(teaId, year, courseId);

        String guide = JsonTools.getJsonParam(params, "guide"); //课程导入
        String teachStep = JsonTools.getJsonParam(params, "teachStep"); //授课过程
        String nextTeach = JsonTools.getJsonParam(params, "nextTeach"); //过渡下一课程
        String blockName = JsonTools.getJsonParam(params, "blockName"); //知识点名称
        String text = JsonTools.getJsonParam(params, "text");
        String delta = JsonTools.getJsonParam(params, "delta");
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
            plan.setOrderNo(queryMaxOrderNo(teaId, courseId, year) + 1);
            plan.setCreateDay(NormalTools.curDate());
            plan.setCreateTime(NormalTools.curDatetime());
            plan.setCreateLong(System.currentTimeMillis());
        }
        plan.setGuide(guide);
        plan.setTeachStep(teachStep);
        plan.setNextTeach(nextTeach);
        plan.setBlockName(blockName);
        plan.setTeachStepText(text);
        plan.setTeachStepDelta(delta);

        plan.setUpdateDay(NormalTools.curDate());
        plan.setUpdateTime(NormalTools.curDatetime());
        plan.setUpdateLong(System.currentTimeMillis());
        teachPlanDao.save(plan);
        return JsonResult.success("保存成功").set("plan", plan);
    }

    private Integer queryMaxOrderNo(Integer teaId, Integer courseId, String year) {
        Integer orderNo = teachPlanDao.queryMaxOrderNo(teaId, courseId, year);
        if(orderNo==null) {orderNo = 0;}
        return orderNo;
    }

    private TeacherClassroom queryClassroom(Integer courseId, String year, Integer teaId) {
        List<TeacherClassroom> list = teacherClassroomDao.queryByCourseId(courseId, year, teaId);
        if(list!=null && list.size()>0) {return list.get(0);}
        return null;
    }

    public JsonResult queryClassroom(String params) {
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Integer classroomId = JsonTools.getIntegerParams(params, "classroomId");
        List<TeacherClassroom> list = null;
        if(classroomId!=null && classroomId>0) {
            list = new ArrayList<>();
            list.add(teacherClassroomDao.findOne(classroomId));
        } else {
            String year = teachPlanConfigTools.getCurYear();
            list = teacherClassroomDao.queryAllByCourseId(courseId, year, teaId);
        }
        return JsonResult.success().set("classroomList", list);
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        TeachPlan plan = teachPlanDao.findOne(id);
        return JsonResult.succ(plan);
    }

    /** 获取教师某课程的教案 */
    public JsonResult listByCourse(String params) {
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        String year = JsonTools.getJsonParam(params, "year");
        Integer id = JsonTools.getId(params);
        List<TeachPlan> planList = teachPlanDao.findByTeacher(teaId, courseId, year, SimpleSortBuilder.generateSort("orderNo_a"));

        return JsonResult.success().set("planList", planList).set("curId", id);
    }

    public JsonResult delete(String params) {
        Integer id = JsonTools.getId(params);
        teachPlanDao.delete(id);
        return JsonResult.success("删除成功");
    }

    /** 修改教案状态 */
    public JsonResult updateFlag(String params) {
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        String flag = JsonTools.getJsonParam(params, "flag");
        String year = teachPlanConfigTools.getCurYear();

        TeachPlanFlag tpf = teachPlanFlagDao.queryOne(teaId, courseId, year);
        if(tpf==null) {
            tpf = new TeachPlanFlag();
            Teacher tea = teacherDao.findOne(teaId);
            ClassCourse cc = classCourseDao.findOne(courseId);
            tpf.setCourseId(cc.getId());
            tpf.setCourseTitle(cc.getTitle());
            tpf.setPlanYear(year);
            tpf.setSchId(tea.getSchoolId());
            tpf.setSchName(tea.getSchoolName());
            tpf.setTeaId(teaId);
            tpf.setTeaName(tea.getName());
            tpf.setTeaPhone(tea.getPhone());
        }
        tpf.setFlag(flag);
        teachPlanFlagDao.save(tpf);
        return JsonResult.success("保存成功").set("planFlag", tpf);
    }

    /** 获取教师对应课程所有已写教案 */
    public JsonResult listPlan(String params) {
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        String year = teachPlanConfigTools.getCurYear();

        List<TeachPlan> planList = teachPlanDao.findByTeacher(teaId, courseId, year, SimpleSortBuilder.generateSort("orderNo_a"));
        return JsonResult.success().set("planList", planList);
    }

    /** 后台管理获取教案信息 */
    public JsonResult listPlan4Manger(String params) {
        Integer schId = JsonTools.getIntegerParams(params, "schId");
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        String type = JsonTools.getJsonParam(params, "type");
        JsonResult result = JsonResult.getInstance();
        if(type==null||"".equals(type) || "1".equals(type)) { //获取学校
            type = "1";
            List<School> schList = schoolDao.findByIsUse("1");
            result.set("schoolList", schList);
        } else if("2".equals(type)) {
            List<Teacher> teaList = teacherDao.findBySchoolIdAndIsUse(schId, "1");
            result.set("teacherList", teaList).set("school", schoolDao.findOne(schId));
        } else if("3".equals(type)) {
            String year = teachPlanConfigTools.getCurYear();
            List<TeachPlan> planList = teachPlanDao.findByTea(teaId, year);
            result.set("year", year).set("planList", planList).set("school", schoolDao.findOne(schId))
            .set("teacher", teacherDao.findOne(teaId));
        }
        return result.set("type", type);
    }
}
