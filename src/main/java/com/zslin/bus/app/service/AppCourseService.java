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
import com.zslin.bus.yard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zsl on 2019/9/11.
 */
@Service
public class AppCourseService {

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private ICourseRecordDao courseRecordDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IAppCourseCommentDao appCourseCommentDao;

    @Autowired
    private IClassTagDao classTagDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    /**
     * 通过标签获取课程
     * @param params 必须包含tagId
     * @return
     */
    public JsonResult listCourseByTag(String params) {
        Integer tagId = Integer.parseInt(JsonTools.getJsonParam(params, "tagId"));
        List<ClassCourse> courseList = classCourseDao.findByTagId(tagId);
        return JsonResult.success().set("courseList", courseList).set("tag", classTagDao.findOne(tagId));
    }

    /**
     * 根据年级获取课程
     * @param params 必须包含年级ID
     * @return
     */
    public JsonResult listCourse(String params) {
        Integer gid = Integer.parseInt(JsonTools.getJsonParam(params, "gid")); //Grade ID
        Integer tagId = null;
        try { tagId = Integer.parseInt(JsonTools.getJsonParam(params, "tagId")); } catch (Exception e) { }
        Integer page = 0;
        try {
            page = Integer.parseInt(JsonTools.getJsonParam(params, "page"));
        } catch (Exception e) {
            page = 0;
        }
        String phone = JsonTools.getJsonParam(params, "phone"); //电话
        JsonResult result = JsonResult.success();
//        System.out.println("========gid::"+gid+", PHONE::"+phone);
        if(gid==0) { //访问历史
            SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("teaPhone", "eq", phone);
            Page<CourseRecord> data = courseRecordDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("lastLong_d")));
//            System.out.println("----------->length::"+data.getTotalElements());
            result.set("data", data.getContent()).set("size", data.getTotalElements());
        } else if(gid==-1) { //如果是测试教师
            SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("showTest", "eq", "1");
            Page<ClassCourse> data = classCourseDao.findAll(ssb.generate(), SimplePageBuilder.generate(page));
            result.set("data", data.getContent()).set("size", data.getTotalElements());
        } else if(gid==-2) { //如果是属于标签课程
            List<ClassCourse> courseList = classCourseDao.findByTagId(tagId);
            result.set("data", courseList).set("size", courseList.size());
        } else {
            SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("sid", "eq", gid);
            Page<ClassSystemDetail> datas = classSystemDetailDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo", "sectionNo")));
            result.set("data", buildData(datas.getContent())).set("size", datas.getTotalElements());
        }
        result.set("gid", gid);
        return result;
    }

    private List<ClassCourse> buildData(List<ClassSystemDetail> detailList) {
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

    /**
     * 获取课程
     * @param params 必须包含课程Id和教师电话
     * @return
     */
    public JsonResult loadCourse(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cid")); //Course ID
        String teaPhone = JsonTools.getHeaderParams(params, "phone"); //教师电话
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

        return JsonResult.success().set("course", course).set("video", video).set("ppt", ppt).set("learn", learn)
                .set("commentList", commentList.getContent()).set("commentCount", commentList.getTotalElements());
    }
}
