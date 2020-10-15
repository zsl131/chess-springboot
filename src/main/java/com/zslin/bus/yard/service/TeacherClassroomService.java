package com.zslin.bus.yard.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IGradeDao;
import com.zslin.bus.yard.dao.ITeachPlanConfigDao;
import com.zslin.bus.yard.dao.ITeacherClassroomDao;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.Grade;
import com.zslin.bus.yard.model.TeachPlanConfig;
import com.zslin.bus.yard.model.Teacher;
import com.zslin.bus.yard.model.TeacherClassroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AdminAuth(name = "教师班级管理", psn = "科普进校园", orderNum = 1, type = "1", url = "/yard/teacherClassroom")
public class TeacherClassroomService {

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IGradeDao gradeDao;

    @Autowired
    private ITeachPlanConfigDao teachPlanConfigDao;

    /** 用于配置班级 */
    public JsonResult findOwn(String params) {
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Teacher tea = teacherDao.findOne(teaId);

        TeachPlanConfig config = teachPlanConfigDao.loadOne();

//        QueryListDto qld = QueryTools.buildQueryListDto(params);
        /*Page<TeacherClassroom> res = teacherClassroomDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("teaId", "eq", teaId)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));*/

        List<TeacherClassroom> classroomList = teacherClassroomDao
                .findByTargetYearAndTeaId(config==null?NormalTools.curDate("yyyy"):config.getConfigYear(), teaId);

        //获取教师可选择的年级角色
        List<Grade> gradeList = gradeDao.findByTeacherFlag(SimpleSortBuilder.generateSort("orderNo_a"));
        return JsonResult.success().set("classroomList", classroomList).set("teacher", tea)
                .set("gradeList", gradeList)
                .set("config", config);
    }

    /** 添加或修改班级 */
    public JsonResult addOrUpdate(String params) {
        Integer id = JsonTools.getId(params);
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Integer gradeId = JsonTools.getIntegerParams(params, "gradeId");
        String roomName = JsonTools.getJsonParam(params, "roomName");
        TeachPlanConfig config = teachPlanConfigDao.loadOne();
        if(id==null || id<=0) { //表示是新增
            Teacher tea = teacherDao.findOne(teaId);
            Grade grade = gradeDao.findOne(gradeId);
            TeacherClassroom classroom = new TeacherClassroom();
            classroom.setGradeId(gradeId);
            classroom.setGradeName(grade.getName());
            classroom.setSid(grade.getSid());
            classroom.setSname(grade.getSname());
            classroom.setRoomName(roomName);
            if(config!=null) {
                classroom.setTargetYear(config.getConfigYear());
                classroom.setTerm(config.getTerm());
            }
            classroom.setTeaId(teaId);
            if(tea!=null) {
                classroom.setTeaName(tea.getName());
                classroom.setTeaPhone(tea.getPhone());
            }
            teacherClassroomDao.save(classroom);
        } else {
            TeacherClassroom classroom = teacherClassroomDao.findOne(id);
            classroom.setRoomName(roomName);
            teacherClassroomDao.save(classroom);
        }
        List<TeacherClassroom> classroomList = teacherClassroomDao
                .findByTargetYearAndTeaId(config==null?NormalTools.curDate("yyyy"):config.getConfigYear(), teaId);
        return JsonResult.success("保存成功").set("classroomList", classroomList);
    }
}
