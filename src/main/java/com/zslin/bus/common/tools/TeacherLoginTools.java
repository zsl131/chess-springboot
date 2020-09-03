package com.zslin.bus.common.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassSystemDao;
import com.zslin.bus.yard.dao.ITeachPlanConfigDao;
import com.zslin.bus.yard.dao.ITeacherClassroomDao;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.TeachPlanConfig;
import com.zslin.bus.yard.model.Teacher;
import com.zslin.bus.yard.model.TeacherClassroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeacherLoginTools {

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    @Autowired
    private ITeachPlanConfigDao teachPlanConfigDao;

   /**
     * 用户登陆时需要检测教师信息
     * @param username 即电话号码
     */
    public JsonResult teacherLogin(String username) {
        JsonResult result = JsonResult.getInstance();
        List<ClassSystem> list = null;
        Teacher tea = teacherDao.findByPhone(username);
        if(tea!=null) { //说明是教师用户
            String isTest = teacherDao.queryIsTest(username); //是否为测试教师
            if (!"1".equals(isTest)) { //不是测试教师
                TeachPlanConfig config = teachPlanConfigDao.loadOne();
                String year = config==null?NormalTools.curDate("yyyy"):config.getConfigYear(); //获取年份
//                list = teacherGradeDao.findGradeByPhone(phone, SimpleSortBuilder.generateSort("orderNo"));
                List<TeacherClassroom> classroomList = teacherClassroomDao.findByTargetYearAndTeaId(year, tea.getId());
                result.set("classroomList", classroomList);
                list = classSystemDao.findByPhone(username, SimpleSortBuilder.generateSort("orderNo"));
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            if ("1".equals(isTest)) { //如果是测试教师
                ClassSystem g = new ClassSystem();
                g.setName("测试教师用户");
                g.setId(-1);
                g.setOrderNo(-1);
                list.add(g);
            }
            ClassSystem g = new ClassSystem();
            g.setName("访问历史");
            g.setId(0);
            g.setOrderNo(0);
            list.add(g);
            result.set("isTeacher", true).set("systemList", list).set("teacher", tea);
        } else {
            result.set("isTeacher", false);
        }
        return result;
    }
}
