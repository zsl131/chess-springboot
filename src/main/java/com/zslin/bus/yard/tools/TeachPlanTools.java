package com.zslin.bus.yard.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassSystemDetailDao;
import com.zslin.bus.yard.dao.ITeachPlanDao;
import com.zslin.bus.yard.dao.ITeacherClassroomDao;
import com.zslin.bus.yard.dto.PlanCourseDto;
import com.zslin.bus.yard.dto.PlanDto;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystemDetail;
import com.zslin.bus.yard.model.TeachPlan;
import com.zslin.bus.yard.model.TeacherClassroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 教案工具
 */
@Component
public class TeachPlanTools {

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    @Autowired
    private ITeachPlanDao teachPlanDao;

    @Autowired
    private TeachPlanConfigTools teachPlanConfigTools;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    public void buildIndex(Integer teaId) {
        String year = teachPlanConfigTools.getCurYear();
        //获取教师所管理的班级
        List<TeacherClassroom> classroomList = teacherClassroomDao.findByTargetYearAndTeaId(year, teaId);

        //获取教师所写的教案
        List<TeachPlan> planList = teachPlanDao.findByTeacher(teaId, year, SimpleSortBuilder.generateSort("sid_a"));

        //教师所管辖的体系ID
        List<Integer> systemIds = buildSystemIds(classroomList);

        List<ClassSystemDetail> detailList = classSystemDetailDao.findByIds(systemIds, SimpleSortBuilder.generateSort("orderNo"));

        //课程列表
//        List<ClassCourse> courseList = buildCourse(detailList);

        List<PlanCourseDto> courseDtoList = buildCourseDto(classroomList, detailList);
        System.out.println(courseDtoList);

        List<PlanDto> planDtoList = buildPlanDto(classroomList, planList);
        System.out.println(planDtoList);
    }

    /** 获取教师对应的班级信息 */
    public List<TeacherClassroom> queryClassroom(Integer teaId) {
        String year = teachPlanConfigTools.getCurYear();
        //获取教师所管理的班级
        return teacherClassroomDao.findByTargetYearAndTeaId(year, teaId);
    }

    private List<TeacherClassroom> rebuild(List<TeacherClassroom> classroomList) {
        List<Integer> sid = new ArrayList<>();
        List<TeacherClassroom> result = new ArrayList<>();
        for(TeacherClassroom tc : classroomList) {
            if(!sid.contains(tc.getSid())) {sid.add(tc.getSid()); result.add(tc);}
        }
        return result;
    }

    public List<PlanCourseDto> queryCourseDto(List<TeacherClassroom> classroomList, boolean roomNoRepeat) {
        try {
            if(roomNoRepeat) {
                classroomList = rebuild(classroomList);
            }
            //教师所管辖的体系ID
            //List<Integer> systemIds = buildSystemIds(classroomList);

            List<PlanCourseDto> result = new ArrayList<>();
            for(TeacherClassroom tc : classroomList) {
                Integer sid = tc.getSid();
                PlanCourseDto pcd = query(result, sid);
                if(pcd==null) {
                    List<ClassSystemDetail> detailList = classSystemDetailDao.findBySid(sid, SimpleSortBuilder.generateSort("orderNo", "sectionNo"));
                    List<ClassCourse> courseList = buildCourse(detailList);
                    List<Integer> courseIds = buildCourseIds(courseList);
                    result.add(new PlanCourseDto(tc, sid, tc.getId(), courseList, courseIds));
                } else {
//                    pcd.setClassroom(tc);
//                    pcd.setClassroomId(tc.getId());
                    result.add(new PlanCourseDto(tc, sid, tc.getId(), pcd.getCourseList(), pcd.getCourseIds()));
                }
            }
            return result;

            //classSystemDetailDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo", "sectionNo")));
//            List<ClassSystemDetail> detailList = classSystemDetailDao.findByIds(systemIds, SimpleSortBuilder.generateSort("orderNo", "sectionNo"));
//            return buildCourseDto(classroomList, detailList);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private PlanCourseDto query(List<PlanCourseDto> list, Integer sid) {
        PlanCourseDto res = null;
        for(PlanCourseDto pcd : list) {
            if(pcd.getSid().equals(sid)) {res = pcd; break;}
        }
        return res;
    }

    private List<Integer> buildCourseIds(List<ClassCourse> courseList) {
        List<Integer> result = new ArrayList<>();
        for(ClassCourse cc : courseList) {result.add(cc.getId());}
        return result;
    }

    private List<ClassCourse> buildCourse(List<ClassSystemDetail> detailList) {
        if(detailList==null || detailList.size()<=0) {return new ArrayList<>();}
        List<Integer> ids = detailList.stream().map(ClassSystemDetail::getCourseId).collect(Collectors.toList());
        ids = rebuildIds(ids); //去除null
        List<ClassCourse> list = classCourseDao.findByIds(ids);
        List<ClassCourse> result = new ArrayList<>();
        for(ClassSystemDetail csd : detailList) { //排序
//            System.out.println(csd.getName());
            for(ClassCourse cc : list) {
                try { //避免null导致的异常
                    if(csd.getCourseId().equals(cc.getId())) {
                        result.add(cc);
                    }
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    //去除null
    private List<Integer> rebuildIds(List<Integer> ids) {
        List<Integer> result = new ArrayList<>();
        for(Integer id : ids) {
            if(id!=null && id>0) {result.add(id);}
        }
        return result;
    }

    private TeacherClassroom queryClassroom(List<TeacherClassroom> classroomList, int sid) {
        TeacherClassroom res = null;
        for(TeacherClassroom tc : classroomList) {if(sid==tc.getSid()) {res = tc; break;}}
        return res;
    }

    /** 获取教师对应的课程 */
    public List<PlanCourseDto> queryCourseDto(List<TeacherClassroom> classroomList) {
        return queryCourseDto(classroomList, true);
    }

    /** 获取已写教案 */
    public List<PlanDto> queryPlan(List<TeacherClassroom> classroomList) {
        try {
            classroomList = rebuild(classroomList);
            TeacherClassroom classroom = classroomList.get(0);
            //获取教师所写的教案
            List<TeachPlan> planList = teachPlanDao.findByTeacher(classroom.getTeaId(), classroom.getTargetYear(), SimpleSortBuilder.generateSort("sid_a"));
            return buildPlanDto(classroomList, planList);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<PlanCourseDto> buildCourseDto(List<TeacherClassroom> classroomList, List<ClassSystemDetail> detailList) {
        List<PlanCourseDto> result = new ArrayList<>();
        for(TeacherClassroom tc : classroomList) {
            result.add(new PlanCourseDto(tc));
        }
        for(ClassSystemDetail csd : detailList) {
            for(PlanCourseDto pcd : result) {
                if(pcd.getSid().equals(csd.getSid())) {pcd.add(csd.getCourseId());} //如果是一个体系的则添加进去
            }
        }
        //重新生成课程列表
        for(PlanCourseDto pcd : result) {
            pcd.setCourseList(classCourseDao.findByIds(pcd.getCourseIds()));
        }
        return result;
    }

    /** 获取教案信息 */
    private List<PlanDto> buildPlanDto(List<TeacherClassroom> classroomList, List<TeachPlan> planList) {
        List<PlanDto> result = new ArrayList<>();
        for(TeacherClassroom tc : classroomList) {
            result.add(new PlanDto(tc));
        }
        for(TeachPlan tp : planList) {
            for(PlanDto pd : result) {
                if(tp.getSid().equals(pd.getSid())) {pd.add(tp);} //如果是一个体系的则添加进去
            }
        }
        return result;
    }

    /*private List<ClassCourse> buildCourse(List<ClassSystemDetail> detailList) {
        if(detailList==null || detailList.size()<=0) {return new ArrayList<>();}
        List<Integer> ids = detailList.stream().map(ClassSystemDetail::getCourseId).collect(Collectors.toList());
        return classCourseDao.findByIds(ids);
    }*/

    /** 生成教师所管辖的体系ID */
    private List<Integer> buildSystemIds(List<TeacherClassroom> classrooms) {
        List<Integer> res = new ArrayList<>();
        for(TeacherClassroom c : classrooms) {
            if(!res.contains(c.getSid())) {res.add(c.getSid());}
        }
        return res;
    }
}
