package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassImageDao;
import com.zslin.bus.yard.model.ClassImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppClassImageService {

    @Autowired
    private IClassImageDao classImageDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    public JsonResult listByCourse(String params) {
        Integer page = 0;
        try { page = Integer.parseInt(JsonTools.getJsonParam(params, "page")); } catch (Exception e) { } //页码

        Integer courseId = JsonTools.getId(params);
        String phone = JsonTools.getJsonParam(params, "phone");
        String year = JsonTools.getJsonParam(params, "year");
        year = (year==null||"".equals(year))? NormalTools.curDate("yyyy"):year; //默认今年

        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        ssb.add(new SpecificationOperator("courseId", "eq", courseId));
        ssb.add(new SpecificationOperator("teaPhone", "eq", phone));
        ssb.add(new SpecificationOperator("createYear", "eq", year));
        Page<ClassImage> list = classImageDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));

        return JsonResult.success().set("imageList", list.getContent()).set("course", classCourseDao.findOne(courseId)).set("year", year);
    }
}
