package com.zslin.bus.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.bus.basic.dao.IActivityCommentDao;
import com.zslin.bus.basic.dao.IActivityDao;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityComment;
import com.zslin.bus.basic.model.ActivityRecord;
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

    /** 获取当前活动 */
    @GetMapping(value = "current")
    public String current(Model model, HttpServletRequest request) {
        Integer id = activityRecordDao.findCurrentActivityId();
        if(id!=null && id>0) {
            return "redirect:/wx/activity/show?id=" + id;
        } else {
            return "weixin/activity/noCurrent";
        }
    }

    @GetMapping(value = "index")
    public String index(Model model, Integer page, HttpServletRequest request) {
        Page<Activity> datas = activityDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request, new SpecificationOperator("status", "eq", "1")),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
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
