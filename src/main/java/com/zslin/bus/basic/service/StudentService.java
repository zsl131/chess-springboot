package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.*;
import com.zslin.bus.basic.model.*;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.DictionaryTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.model.WxAccount;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/8/7.
 */
@Service
@AdminAuth(name = "学生管理", psn = "系统管理", orderNum = 3, type = "1", url = "/admin/student")
public class StudentService {

    @Autowired
    private IStudentDao studentDao;

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private IAgeDicDao ageDicDao;

    @Autowired
    private ISchoolDicDao schoolDicDao;

    @Autowired
    private IActivityStudentDao activityStudentDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Student> res = studentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        List<AgeDic> ageList = ageDicDao.findAll();
        List<SchoolDic> schoolList = schoolDicDao.findAll();
        return JsonResult.getInstance()
                .set("size", (int) res.getTotalElements()).set("datas", res.getContent())
                .set("ageList", ageList).set("schoolList", schoolList);
    }

    /**
     * 获取当前报名的活动开展ID
     * @param params {'recordId': 1, 'openid':'abc123'}
     * @return
     */
    public JsonResult loadActivityRecord(String params) {
        Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId"));
        String openid = JsonTools.getJsonParam(params, "openid");

        WxAccount account = wxAccountDao.findByOpenid(openid);

        ActivityRecord ar = activityRecordDao.findOne(recordId);

        List<Student> stuList = studentDao.findByOpenid(openid); //获取用户已经绑定的学生信息

        List<ActivityStudent> applyList = activityStudentDao.findByRecordIdAndOpenid(recordId, openid);

        List<AgeDic> ageList = ageDicDao.findAll(SimpleSortBuilder.generateSort("orderNo_a"));

        List<SchoolDic> schoolList = schoolDicDao.findAll(SimpleSortBuilder.generateSort("orderNo_a"));

        return JsonResult.getInstance().set("record", ar).set("stuList", stuList)
                .set("account", account).set("applyList", applyList)
                .set("ageList", new DictionaryTools<AgeDic>().buildDictionaryDtoList(ageList))
                .set("schoolList", new DictionaryTools<SchoolDic>().buildDictionaryDtoList(schoolList));
    }

    public JsonResult loadObj(String params) {
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        Student stu = studentDao.findOne(id);
        if(stu!=null) {
            return JsonResult.succ(stu);
        } else {
            return JsonResult.error("数据不存在");
        }
    }

    public JsonResult deleteObj(String params) {
        System.out.println("===="+params);
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        if(activityStudentDao.findByStuId(id).size()>0) {return JsonResult.error("存在报名信息，不可删除");}
        else {
            studentDao.delete(id);
            return JsonResult.success("删除成功");
        }
    }

    public JsonResult updateObj(String params) {
        try {
            Student stu = JSON.toJavaObject(JSON.parseObject(params), Student.class);
            Student student = studentDao.findOne(stu.getId());

            student.setName(stu.getName().replaceAll(" ", "")); //替换空格
            student.setAgeName(ageDicDao.findNameById(stu.getAgeId()));
            student.setPhone(stu.getPhone().replace(" ", ""));
            student.setSchoolName(schoolDicDao.findNameById(stu.getSchoolId()));
            student.setAgeId(stu.getAgeId());
            student.setSchoolId(stu.getSchoolId());
            student.setSex(stu.getSex());
            WxAccount account = wxAccountDao.findByOpenid(stu.getOpenid());
            if(account!=null) {
                student.setAvatarUrl(account.getAvatarUrl());
                student.setNickname(account.getNickname());
            }
            studentDao.save(student);
            return JsonResult.succ(student);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 只做添加学生 */
    public JsonResult addStudentOnly(String params) {
        Student stu = JSON.toJavaObject(JSON.parseObject(params), Student.class);
        stu.setName(stu.getName().replaceAll(" ", "")); //替换空格
        stu.setAgeName(ageDicDao.findNameById(stu.getAgeId()));
        stu.setPhone(stu.getPhone().replace(" ", ""));
        stu.setSchoolName(schoolDicDao.findNameById(stu.getSchoolId()));
        WxAccount account = wxAccountDao.findByOpenid(stu.getOpenid());
        if(account!=null) {
            stu.setAvatarUrl(account.getAvatarUrl());
            stu.setNickname(account.getNickname());
        }
        stu.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
        stu.setCreateTime(NormalTools.curDatetime());
        stu.setCreateLong(System.currentTimeMillis());
        studentDao.save(stu);
        return JsonResult.succ(stu);
    }

    /** 添加学生 */
    public JsonResult addStudent(String params) {
//        System.out.println("----studentService.addStudent:::"+params);
        //
        Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId")); //开展记录ID
        String from = JsonTools.getJsonParam(params, "from");
        Student stu = JSON.toJavaObject(JSON.parseObject(params), Student.class);
        stu.setName(stu.getName().replaceAll(" ", "")); //替换空格
        stu.setAgeName(ageDicDao.findNameById(stu.getAgeId()));
        stu.setPhone(stu.getPhone().replace(" ", ""));
        stu.setSchoolName(schoolDicDao.findNameById(stu.getSchoolId()));
        try {
            WxAccount account = wxAccountDao.findByOpenid(stu.getOpenid());
            stu.setAvatarUrl(account.getAvatarUrl());
            stu.setNickname(account.getNickname());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        stu.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
        stu.setCreateTime(NormalTools.curDatetime());
        stu.setCreateLong(System.currentTimeMillis());
        studentDao.save(stu);

        //添加报名信息
        ActivityRecord ar = activityRecordDao.findOne(recordId);
        ActivityStudent as = new ActivityStudent();
        as.setAvatarUrl(stu.getAvatarUrl());
        as.setActId(ar.getActId());
        as.setActTitle(ar.getActTitle());
        as.setHoldTime(ar.getHoldTime());
        as.setOpenid(stu.getOpenid());
        as.setRecordId(recordId);
        as.setFromFlag(from);
        as.setStatus("0");
        as.setStuId(stu.getId());
        as.setDepId(ar.getDepId());
        as.setAddress(ar.getAddress());
        as.setDepName(ar.getDepName());
        as.setStuName(stu.getName());
        as.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));

        as.setAgeId(stu.getAgeId());
        as.setAgeName(stu.getAgeName());
        as.setSchoolName(stu.getSchoolName());
        as.setSchoolId(stu.getSchoolId());
        as.setSex(stu.getSex());
        as.setPhone(stu.getPhone());

        activityStudentDao.save(as);

        activityRecordDao.updateApplyCount(recordId, 1); //报名人数加1

        return JsonResult.success("报名成功，等待审核");
    }

    /**
     * 删除申请
     * @param params {"openid":"abc123", "id": 1}
     * @return
     */
    public JsonResult deleteApply(String params) {
        try {
            String openid = JsonTools.getJsonParam(params, "openid");
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            ActivityStudent as = activityStudentDao.findOne(id);
            if(!as.getOpenid().equals(openid)) {
                return JsonResult.error("无权限操作");
            }
            activityRecordDao.updateApplyCount(as.getRecordId(), -1); //报名人数-1
            activityStudentDao.delete(as);
            return JsonResult.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 批量删除学生信息
     * @param params {"openid":"abc123", objIds: [1, 2]}
     * @return
     */
    public JsonResult deleteBatch(String params) {
        String openid = JsonTools.getJsonParam(params, "openid");
        String objIds = JsonTools.getJsonParam(params, "objIds");
        JSONArray array = JsonTools.str2JsonArray(objIds);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<array.size();i++) {
            Integer stuId = array.getInteger(i);
            if(activityStudentDao.findByStuId(stuId).size()>0) {sb.append(studentDao.findStuName(stuId)).append( "存在报名记录；");}
            else {studentDao.deleteByOpenidAndId(openid, stuId);}
        }
        return JsonResult.success("操作完成。"+sb.toString());
    }

    /**
     * 批量报名参加活动
     * @param params {"openid":"abc123", objIds: [1,2], recordId:1}
     * @return
     */
    public JsonResult applyBatch(String params) {
        Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId")); //开展记录ID
        String from = JsonTools.getJsonParam(params, "from");
        //添加报名信息
        ActivityRecord ar = activityRecordDao.findOne(recordId);

        String objIds = JsonTools.getJsonParam(params, "objIds");
        JSONArray array = JsonTools.str2JsonArray(objIds);
        for(int i=0;i<array.size();i++) {
            Integer stuId = array.getInteger(i);
            if(activityStudentDao.findByStuIdAndRecordId(stuId, recordId)!=null) {continue;}
            ActivityStudent as = new ActivityStudent();
            Student stu = studentDao.findOne(stuId);
            as.setAvatarUrl(stu.getAvatarUrl());
            as.setActId(ar.getActId());
            as.setFromFlag(from);
            as.setActTitle(ar.getActTitle());
            as.setHoldTime(ar.getHoldTime());
            as.setOpenid(stu.getOpenid());
            as.setAddress(ar.getAddress());
            as.setRecordId(recordId);
            as.setStatus("0");
            as.setDepId(ar.getDepId());
            as.setDepName(ar.getDepName());
            as.setStuId(stu.getId());
            as.setStuName(stu.getName());

            as.setAgeId(stu.getAgeId());
            as.setAgeName(stu.getAgeName());
            as.setSchoolName(stu.getSchoolName());
            as.setSchoolId(stu.getSchoolId());
            as.setSex(stu.getSex());
            as.setPhone(stu.getPhone());
            as.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));

            activityStudentDao.save(as);

            activityRecordDao.updateApplyCount(recordId, 1); //报名人数加1
        }

        return JsonResult.success("报名成功，等待审核");
    }
}
