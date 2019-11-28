package com.zslin.bus.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.bus.basic.dao.IActivityDao;
import com.zslin.bus.basic.dao.IActivityStudentDao;
import com.zslin.bus.basic.dao.INoticeDao;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityStudent;
import com.zslin.bus.basic.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2019/7/6.
 */
@Controller
@RequestMapping(value = "weixin")
public class WeixinIndexController {

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private INoticeDao noticeDao;

    @GetMapping(value = "index")
    public String index(Model model, Integer page, HttpServletRequest request) {
        Integer zeroCount = activityDao.recordCountZero();
        Integer recordCount = activityDao.recordCount();
        model.addAttribute("recordCount", zeroCount+recordCount); //活动开展次数
//        model.addAttribute("stuCount", activityStudentDao.countAll()); //所有学员报名人次
//        model.addAttribute("passedCount", activityStudentDao.countPassed()); //报名通过的人次

        Page<Activity> activityList = activityDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request, new SpecificationOperator("status", "eq", "1")),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("activityList", activityList);

        Page<ActivityStudent> studentList = activityStudentDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("studentList", studentList);

        Page<Notice> noticeList = noticeDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request, new SpecificationOperator("status", "eq", "1")),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("noticeList", noticeList);

        return "weixin/index";
    }
}
