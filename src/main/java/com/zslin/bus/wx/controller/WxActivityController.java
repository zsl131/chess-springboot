package com.zslin.bus.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.bus.basic.dao.*;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityComment;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.basic.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2019/6/23.
 */
@RequestMapping(value = "wx/activity")
@Controller
public class WxActivityController {

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private IActivityCommentDao activityCommentDao;

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private IDepartmentDao departmentDao;

    /** 获取当前活动 */
    @GetMapping(value = "current")
    public String current(Model model, HttpServletRequest request) {
        Integer id = activityRecordDao.findCurrentActivityId();
        if(id!=null && id>0) {
            return "redirect:/wx/activity/show?id=" + id;
        } else {
            Integer zeroCount = activityDao.recordCountZero();
            Integer recordCount = activityDao.recordCount();
            model.addAttribute("recordCount", zeroCount+recordCount); //活动开展次数
            model.addAttribute("stuCount", activityStudentDao.countAll()); //所有学员报名人次
            model.addAttribute("passedCount", activityStudentDao.countPassed()); //报名通过的人次
//
            return "weixin/activity/noCurrent";
        }
    }

    @GetMapping(value = "index")
    public String index(Model model, Integer depId, Integer page, HttpServletRequest request) {
        Page<Activity> datas = activityDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                new SpecificationOperator("status", "eq", "1"),
                (depId==null || depId<=0)?null:new SpecificationOperator("depId", "eq", depId)),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        if(depId!=null && depId>0) {
            Department dep = departmentDao.findOne(depId);
            model.addAttribute("department", dep);
        } else {
            model.addAttribute("department", null);
        }
        model.addAttribute("datas", datas);
        return "weixin/activity/index";
    }

    @GetMapping(value = "show")
    public String show(Model model, Integer id, HttpServletRequest request) {
        Activity activity = activityDao.findOne(id);
        List<ActivityComment> commentList = activityCommentDao.listByActivityId(id);
        List<ActivityRecord> recordList = activityRecordDao.findByActId(id);
        model.addAttribute("activity", activity);
        model.addAttribute("commentList", commentList);
        model.addAttribute("recordList", recordList);
        return "weixin/activity/show";
    }
}
