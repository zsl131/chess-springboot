package com.zslin.bus.wx.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.*;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.basic.model.ActivityStudent;
import com.zslin.bus.basic.model.Student;
import com.zslin.bus.common.rabbit.RabbitMQConfig;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.model.WxAccount;
import com.zslin.bus.wx.tools.ScoreTools;
import com.zslin.bus.wx.tools.SessionTools;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2019/6/23.
 */
@RequestMapping(value = "wx/activityRecord")
@Controller
public class WxActivityRecordController {

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private IStudentDao studentDao;

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private ISchoolDicDao schoolDicDao;

    @Autowired
    private IAgeDicDao ageDicDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /** 签到 */
    @GetMapping(value = "signIn")
    public String signIn(Model model, Integer recordId, String phone, HttpServletRequest request) {
        ActivityRecord record = activityRecordDao.findOne(recordId);
        model.addAttribute("record", record);
        phone = phone==null?"":phone;
        phone = phone.replaceAll(" ", "");
        String openid = SessionTools.getOpenid(request);
        List<ActivityStudent> actList ;
        if(phone!=null && !"".equals(phone)) {
            actList = activityStudentDao.findByRecordIdAndPhone(recordId, phone);
        } else {
            actList = activityStudentDao.findByRecordIdAndOpenid(recordId, openid);
        }
        model.addAttribute("actList", actList);
        return "weixin/activityRecord/signIn";
    }

    @PostMapping(value = "signPost")
    public @ResponseBody String signPost(Integer id, HttpServletRequest request) {
        try {
            ActivityStudent as = activityStudentDao.findOne(id);
            activityStudentDao.udpateHasCheck(id, "1");
            scoreTools.plusScoreByThread("活动签到得积分", as.getOpenid());

            /*templateMessageTools.sendMessageByThread("活动签到通知", as.getOpenid(), "#", "您已成功签到了",
                    TemplateMessageTools.field("学生姓名", as.getStuName()),
                    TemplateMessageTools.field("活动名称", as.getActTitle()),
                    TemplateMessageTools.field("活动地点", as.getAddress()),
                    TemplateMessageTools.field("签到时间", NormalTools.curDate()),
                    TemplateMessageTools.field("签到成功"));*/

            SendMessageDto smd = new SendMessageDto("活动签到通知", as.getOpenid(), "#", "您已成功签到了",
                    TemplateMessageTools.field("学生姓名", as.getStuName()),
                    TemplateMessageTools.field("活动名称", as.getActTitle()),
                    TemplateMessageTools.field("活动地点", as.getAddress()),
                    TemplateMessageTools.field("签到时间", NormalTools.curDate()),
                    TemplateMessageTools.field("签到成功"));
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);

            return "1";
        } catch (Exception e) {
            return "出错："+e.getMessage();
        }
    }

    /** 报名, from: 1-学校；0-社会 */
    @GetMapping(value = "signUp")
    public String signUp(Model model, Integer recordId, String from, HttpServletRequest request) {
        ActivityRecord record = activityRecordDao.findOne(recordId);
        model.addAttribute("record", record);
        model.addAttribute("from", (from!=null && "1".equals(from))?"1":"0");
        String openid = SessionTools.getOpenid(request);
//System.out.println("-------------OPENID: "+openid);
        List<ActivityStudent> actList = activityStudentDao.findByRecordIdAndOpenid(recordId, openid);
        List<Student> studentList = studentDao.findByOpenid(openid);
        model.addAttribute("studentList", studentList);
        model.addAttribute("actList", actList);
        return "weixin/activityRecord/signUp";
    }

    @PostMapping(value = "addStudent")
    public @ResponseBody String addStudent(String name, String phone, String sex, Integer age, Integer school, HttpServletRequest request) {
        try {
            String openid = SessionTools.getOpenid(request);
            Student stu = new Student();
            stu.setOpenid(openid);
            stu.setAgeId(age);
            stu.setSchoolId(school);
            stu.setName(name.replaceAll(" ", "")); //替换空格
            stu.setAgeName(ageDicDao.findNameById(age));
            stu.setPhone(phone.replace(" ", ""));
            stu.setSchoolName(schoolDicDao.findNameById(school));
            WxAccount account = wxAccountDao.findByOpenid(openid);
            if(account!=null) {
                stu.setAvatarUrl(account.getAvatarUrl());
                stu.setNickname(account.getNickname());
            }
            stu.setSex(sex);
            stu.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
            stu.setCreateTime(NormalTools.curDatetime());
            stu.setCreateLong(System.currentTimeMillis());
            studentDao.save(stu);
            return "1";
        } catch (Exception e) {
            return "出错："+e.getMessage();
        }
    }

    /** 报名 */
    @PostMapping(value = "addStudentActivity")
    public @ResponseBody String addStudentActivity(String ids, String from, Integer recordId, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        String [] array = ids.replaceAll(" ", "").split(",");
        ActivityRecord ar = activityRecordDao.findOne(recordId);
        for(String idStr : array) {
            if(idStr!=null) {
                try {
                    Integer id = Integer.parseInt(idStr); //StudentID
                    ActivityStudent as = activityStudentDao.findByStuIdAndRecordId(id, recordId);
                    if(as==null) { //如果没有添加的才可以添加
                        as = new ActivityStudent();
                        Student stu = studentDao.findOne(id);
                        WxAccount account = wxAccountDao.findByOpenid(openid);
                        as.setAgeId(stu.getAgeId());
                        as.setFromFlag(from);
                        as.setSchoolId(stu.getSchoolId());
                        as.setDepName(ar.getDepName());
                        as.setCreateTime(NormalTools.curDatetime());
                        as.setActId(ar.getActId());
                        as.setActTitle(ar.getActTitle());
                        as.setAddress(ar.getAddress());
                        as.setAgeName(stu.getAgeName());
                        if(account!=null) {
                            as.setAvatarUrl(account.getAvatarUrl());
                        }
                        as.setDepId(ar.getDepId());
                        as.setHoldTime(ar.getHoldTime());
                        as.setOpenid(openid);
                        as.setSchoolName(stu.getSchoolName());
                        as.setSex(stu.getSex());
                        as.setStatus("0");
                        as.setRecordId(recordId);
                        as.setPhone(stu.getPhone());
                        as.setStuId(id);
                        as.setStuName(stu.getName());
                        activityStudentDao.save(as);
                        activityRecordDao.updateApplyCount(recordId, 1); //报名人数加1
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("WxActivityRecordController===>Openid:::"+openid);
                    return "出错："+e.getMessage();
                }
            }
        }
        return "1";
    }

    /** 删除学生，只能删除自己添加的学生 */
    @PostMapping(value = "deleteStus")
    public @ResponseBody String deleteStus(String ids, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        String [] array = ids.replaceAll(" ", "").split(",");
        for(String idStr : array) {
            if(idStr!=null) {
                try {
                    Integer id = Integer.parseInt(idStr);
                    studentDao.deleteByOpenidAndId(openid, id);
                } catch (Exception e) {
                }
            }
        }
        return "1";
    }
}
