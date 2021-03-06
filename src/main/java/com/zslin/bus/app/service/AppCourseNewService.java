package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppCourseCommentDao;
import com.zslin.bus.app.model.AppCourseComment;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.dto.ClassImageDto;
import com.zslin.bus.yard.dto.PlanCourseDto;
import com.zslin.bus.yard.model.*;
import com.zslin.bus.yard.tools.ClassImageTools;
import com.zslin.bus.yard.tools.TeachPlanTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppCourseNewService {

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

    @Autowired
    private ITeachPlanFlagDao teachPlanFlagDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private ICourseRecordDao courseRecordDao;

    @Autowired
    private IAppCourseCommentDao appCourseCommentDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IClassCourseAttaDao classCourseAttaDao;

    /** 获取数据 */
    public JsonResult loadData(String params) {
//        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Integer teaId = JsonTools.getUserId(params);
        Teacher tea = teacherDao.findOne(teaId);
        List<TeacherClassroom> classroomList = teachPlanTools.queryClassroom(teaId);

        //按班级来
        List<PlanCourseDto> roomCourseList = teachPlanTools.queryCourseDto(classroomList, false);

        List<TeachPlanFlag> planFlagList = null;
        List<ClassImageDto> imageList = null;
        try {
            TeacherClassroom classroom = classroomList.get(0);
            planFlagList = teachPlanFlagDao.findByTea(teaId, classroom.getTargetYear());

            imageList = classImageTools.build(teaId, classroom.getTargetYear(), classroomList);
        } catch (Exception e) {
        }

        List<YardNotice> noticeList = yardNoticeDao.findShow(SimpleSortBuilder.generateSort("orderNo_a"));
        return JsonResult.success().set("classroomList", classroomList)
                .set("teacher", tea).set("noticeList", noticeList)
                .set("planFlagList", planFlagList).set("roomCourseList", roomCourseList)
                .set("imageList", imageList);
    }

    /**
     * 获取课程
     * @param params 必须包含课程Id和教师电话
     * @return
     */
    public JsonResult loadCourse(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cid")); //Course ID
        String teaPhone = JsonTools.getHeaderParams(params, "phone"); //教师电话
        Integer roomId = JsonTools.getIntegerParams(params, "roomId");
        if(teaPhone==null || "".equals(teaPhone)) {
            teaPhone = JsonTools.getJsonParam(params, "phone");
        }
        Integer page = 0;
        try { page = Integer.parseInt(JsonTools.getJsonParam(params, "page")); } catch (Exception e) { } //页码

        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        ssb.add(new SpecificationOperator("courseId", "eq", cid));
        ssb.add(new SpecificationOperator("status", "eq", "1", "and", new SpecificationOperator("phone", "eq", teaPhone, "or")));
        Page<AppCourseComment> commentList = appCourseCommentDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));

        ClassCourse course = classCourseDao.findOne(cid);
        Teacher tea = teacherDao.findByPhone(teaPhone);

        CourseRecord cr = courseRecordDao.findByCourseIdAndTeaPhone(cid, teaPhone);
        if(cr==null) {
            cr = new CourseRecord();
            cr.setCourseId(cid);
            cr.setFirstDate(NormalTools.curDate());
            cr.setFirstDay(NormalTools.curDate("yyyy-MM-dd"));
            cr.setFirstLong(System.currentTimeMillis());
            cr.setFirstTime(NormalTools.curDatetime());
            cr.setGrade(course.getGrade());
            cr.setGradeId(course.getGradeId());
            cr.setLastDate(NormalTools.curDate());
            cr.setLastDay(NormalTools.curDate("yyyy-MM-dd"));
            cr.setLastLong(System.currentTimeMillis());
            cr.setLastTime(NormalTools.curDatetime());
            cr.setLearnTarget(course.getLearnTarget());
            cr.setTeaId(tea.getId());
            cr.setTeaName(tea.getName());
            cr.setTeaPhone(teaPhone);
            cr.setTitle(course.getTitle());
            courseRecordDao.save(cr);
        } else {
            cr.setLastDate(NormalTools.curDate());
            cr.setLastDay(NormalTools.curDate("yyyy-MM-dd"));
            cr.setLastLong(System.currentTimeMillis());
            cr.setLastTime(NormalTools.curDatetime());
            courseRecordDao.save(cr);
        }

        Attachment video = (course.getVideoId()==null||course.getVideoId()<=0)?null:attachmentDao.findOne(course.getVideoId());
        Attachment ppt = (course.getPptId()==null||course.getPptId()<=0)?null:attachmentDao.findOne(course.getPptId());
        Attachment learn = (course.getLearnId()==null||course.getLearnId()<=0)?null:attachmentDao.findOne(course.getLearnId());
        List<ClassCourseAtta> attaList = classCourseAttaDao.findByCourseId(cid);

        /** 获取多视频 */
        String videoIds = course.getVideoIds();
        videoIds = videoIds==null?"0":"0"+videoIds+"0";
        if(course.getVideoId()!=null&&course.getVideoId()>0) {
            videoIds += ","+course.getVideoId(); //把之前的获取出来
        }
        //System.out.println("-----------ClassCourseService.handleAtta---------"+videoIds);
        List<Attachment> attachmentList = attachmentDao.listByHql("FROM Attachment a WHERE a.id IN ("+videoIds+")");

        TeacherClassroom room = teacherClassroomDao.findOne(roomId);

        return JsonResult.success().set("course", course).set("video", video).set("ppt", ppt).set("learn", learn)
                .set("commentList", commentList.getContent()).set("commentCount", commentList.getTotalElements())
                .set("attaList", attaList).set("room", room).set("attachmentList", attachmentList);
    }
}
