package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IActivityDao;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.dao.IActivityRecordImageDao;
import com.zslin.bus.basic.dao.IActivityStudentDao;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/31.
 */
@Service
@AdminAuth(name = "活动开展记录", psn = "activityService", url = "/admin/activity/record", orderNum = 3)
public class ActivityRecordService {

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private IActivityDao activityDao;

    /*@Autowired
    private IActivityApplyRecordDao activityApplyRecordDao;*/
    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private IActivityRecordImageDao activityRecordImageDao;

    public JsonResult listRecord(String params) {
        Integer actId = Integer.parseInt(JsonTools.getJsonParam(params, "actId"));
        Activity a = activityDao.findOne(actId);

        if(JsonParamTools.isAdminUser(params) || JsonParamTools.containDepId(params, a.getDepId())) {
            QueryListDto qld = QueryTools.buildQueryListDto(params);
            Page<ActivityRecord> res = activityRecordDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), new SpecificationOperator("actId", "eq", actId)),
                    SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
            return JsonResult.getInstance().set("activity", a).set("size", res.getTotalElements()).set("list", res.getContent());
        } else {
            return JsonResult.error("没有权限访问");
        }
    }

    public JsonResult loadOne(String params) {
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        ActivityRecord a = activityRecordDao.findOne(id);
        if(a==null) {
            return JsonResult.getInstance().fail("数据不存在或已被删除");
        } else {
            return JsonResult.getInstance().set("record", a);
        }
    }

    public JsonResult deleteObj(String params) {
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        Integer count = activityStudentDao.applyCount(id);
        if(count!=null && count>0) {
            return JsonResult.getInstance().fail("已有 "+count+" 条申请记录，不可删除");
        } else {
            ActivityRecord ar = activityRecordDao.findOne(id);
            activityRecordDao.delete(ar);
            activityDao.updateRecordCount(ar.getActId(), -1); //减一次活动开展次数
            return JsonResult.getInstance().ok("删除成功");
        }
    }

    public JsonResult addOrUpdate(String params) {
        ActivityRecord ar = JSON.toJavaObject(JSON.parseObject(params), ActivityRecord.class);
        if(ar.getId()==null || ar.getId()<=0) { //添加
            ar.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
            ar.setCreateTime(NormalTools.curDatetime());
            ar.setCreateLong(System.currentTimeMillis());
            ar.setStatus("0");
            ar.setJoinType((ar.getMoney()!=null&&ar.getMoney()>0)?"1":"0");
            activityRecordDao.save(ar);
            activityDao.updateRecordCount(ar.getActId(), 1); //添加一次活动开展次数
//            return new JsonResult(new JsonObj(1, ar));
            return JsonResult.getInstance().set("obj", ar).set("isAdd", "1");
        } else { //修改
            ActivityRecord a = activityRecordDao.findOne(ar.getId());
            a.setPhone(ar.getPhone());
            a.setAddress(ar.getAddress());
            a.setDeadline(ar.getDeadline());
            a.setMaxCount(ar.getMaxCount());
            a.setStatus(ar.getStatus());
            a.setStartTime(ar.getStartTime());
            a.setHoldTime(ar.getHoldTime());
            a.setMoney(ar.getMoney());
            a.setJoinType((ar.getMoney()!=null&&ar.getMoney()>0)?"1":"0");
            if(ar.getPublishDate()!=null && !"".equals(ar.getPublishDate())) {
                a.setPublishDate(ar.getPublishDate());
            } else {
                a.setPublishDate(NormalTools.curDate());
            }
            String holdTime = a.getHoldTime();
            if(holdTime!=null) { //设置日期
                activityRecordImageDao.updateHoldTime(buildLong(holdTime), holdTime, a.getId());
            }
            activityRecordDao.save(a);
//            return new JsonResult(new JsonObj(1, a));
            return JsonResult.getInstance().set("obj", a).set("isAdd", "0");
        }
    }

    private Long buildLong(String holdTime) {
        Long res = NormalTools.str2Long(holdTime, "yyyy-MM-dd HH:mm:ss");
        return res;
    }
}
