package com.zslin.bus.test.controller;

import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassSystemDetailDao;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2019/1/2.
 */
@RestController
@RequestMapping(value = "test/course")
public class TestClassCourseController {

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @GetMapping(value = "init")
    public String init(HttpServletRequest request) {
        List<ClassSystemDetail> list = classSystemDetailDao.findAll();
        for(ClassSystemDetail csd : list) {
            if(csd.getCourseId()!=null && csd.getCourseId()>0 && (csd.getCname() ==null || "".equals(csd.getCname()))) {
                ClassCourse cc = classCourseDao.findOne(csd.getCourseId());
                if(cc!=null) {
                    csd.setCpname(cc.getCpname());
                    csd.setCname(cc.getCname());
                    classSystemDetailDao.save(csd);
                }
            }
        }
        return "the end";
    }
}
