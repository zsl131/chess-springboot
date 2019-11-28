package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IAttachmentDao;
import com.zslin.bus.yard.dao.IClassCategoryDao;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IGradeDao;
import com.zslin.bus.yard.dto.CategoryCourseTreeDto;
import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.tools.CourseTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/12.
 */
@Service
@AdminAuth(name = "科普课程管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/courseNew")
public class ClassCourseNewService {

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IGradeDao gradeDao;

    @Autowired
    private IClassCategoryDao classCategoryDao;

    @Autowired
    private CourseTools courseTools;

    public JsonResult index(String params) {
        JsonResult result = JsonResult.success();
        String type = null;
        Integer id = 0;
        try {
            String pid = JsonTools.getJsonParam(params, "pid");
            String [] array = pid.split("_");
            type = array[0];
            id = Integer.parseInt(array[1]);
        } catch (Exception e) {
        }

        if("root".equalsIgnoreCase(type)) {
            List<ClassCategory> list = classCategoryDao.findByParent(id, SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("category", classCategoryDao.findOne(id)).set("type", type);
        } else if("child".equalsIgnoreCase(type)) {
            List<ClassCourse> list = classCourseDao.findByCid(id);
            result.set("data", list).set("category", classCategoryDao.findOne(id)).set("type", type);
        } else if("course".equalsIgnoreCase(type)) {
            ClassCourse s = classCourseDao.findOne(id);
            if(s.getLearnId()!=null && s.getLearnId()>0) {result.set("learn", attachmentDao.findOne(s.getLearnId()));}
            if(s.getPptId()!=null && s.getPptId()>0) {result.set("ppt", attachmentDao.findOne(s.getPptId()));}
            if(s.getVideoId()!=null && s.getVideoId()>0) {result.set("video", attachmentDao.findOne(s.getVideoId()));}
            result.set("category", "").set("gradeList", gradeDao.findAll(SimpleSortBuilder.generateSort("orderNo_a"))).set("obj", s).set("type", type);
        } else {
            List<ClassCategory> list = classCategoryDao.findRoot(SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("category", "").set("type", "base");
        }

        List<CategoryCourseTreeDto> treeData = courseTools.buildCourseTree();
        result.set("treeData", treeData);
        return result;
    }
}
