package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.*;
import com.zslin.bus.basic.iservice.IActivityService;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityComment;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.basic.model.Department;
import com.zslin.bus.common.dto.QueryListConditionDto;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/7/12.
 */
@Service
@AdminAuth(name = "活动内容管理", psn = "系统管理", url = "/admin/activity", type = "1", orderNum = 3)
public class ActivityService implements IActivityService {

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private IDepUserDao depUserDao;

    @Autowired
    private IDepartmentDao departmentDao;

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private IActivityApplyRecordDao activityApplyRecordDao;

    @Autowired
    private IActivityCommentDao activityCommentDao;

    public JsonResult listDep(String params) {
        try {
            List<Department> depList ;
            if(JsonParamTools.isAdminUser(params)) {
                depList = departmentDao.findAll();
            } else {
                Integer [] depsIds = JsonParamTools.getDepIds(params);
                depList = departmentDao.findByIds(depsIds);
            }
            return JsonResult.getInstance().set("size", depList.size()).set("datas", depList);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Activity> res = activityDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), JsonParamTools.buildAuthDepSpe(params)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        JsonResult result = JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
        Integer depId = null;
        try { depId = Integer.parseInt(JsonTools.getJsonParam(params, "_depId")); } catch (Exception e) { }
        if(depId!=null && depId>0) {
            result.set("department", departmentDao.findOne(depId));
        }
        return result;
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Activity obj = activityDao.findOne(id);

            activityDao.updateReadCount(id, 1);
            return JsonResult.succ(obj);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 当网友点赞 */
    public JsonResult onGood(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            activityDao.updateGoodCount(id, 1);
            return JsonResult.success("点赞成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 微信端获取推荐的活动信息 */
    public JsonResult listRecommend(String params) {
        Integer length = 10;
        try { length = Integer.parseInt(JsonTools.getJsonParam(params, "length")); } catch (Exception e) {}

        QueryListDto qld = new QueryListDto(0, length, "readCount_d", new QueryListConditionDto("status", "eq", "1"));
        Page<Activity> res = activityDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("activityList", res.getContent());
    }

    /** 微信端的详情，需要获取评论等信息 */
    public JsonResult loadOne4Wx(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Activity obj = activityDao.findOne(id);

            String openid = JsonTools.getJsonParam(params, "openid");

            QueryListDto qld = QueryTools.buildQueryListDto(params);
            Page<ActivityComment> res = activityCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                    new SpecificationOperator("actId", "eq", id) //活动详情Id
                    , new SpecificationOperator("status", "eq", "1",  new SpecificationOperator("openid", "eq", openid, "or")) //状态为显示
//                    , new SpecificationOperator("openid", "eq", openid, "or") //或openid为当前用户， 暂时去掉此功能
                    ),
                    SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

            activityDao.updateReadCount(id, 1);

            List<ActivityRecord> recordList = activityRecordDao.findByActId(id); //获取活动记录

            return JsonResult.getInstance().set("obj", obj).set("commentSize", res.getTotalElements()).set("commentList", res.getContent()).set("totalPage", res.getTotalPages())
                    .set("recordList", recordList).set("recordSize", recordList.size());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 修改量，如：点击量等
     * @param params {id:1, field:readCount, amount:1}
     * @return
     */
    public JsonResult updateCount(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id")); //id
            String field = JsonTools.getJsonParam(params, "field"); //字段
            Integer amount = Integer.parseInt(JsonTools.getJsonParam(params, "amount")); //增量
            activityDao.updateByHql("UPDATE Activity a SET a."+field+"=a."+field+"+"+amount+" WHERE a.id="+id);
            return JsonResult.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @AdminAuth(name = "添加或修改", orderNum = 1)
    public JsonResult addOrUpdate(String params) {
        try {
            Activity act = JSONObject.toJavaObject(JSON.parseObject(params), Activity.class);
            if(act.getId()==null || act.getId()<=0) { //新增
                Department d = departmentDao.findOne(act.getDepId());
                act.setDepName(d.getName());
                act.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
                act.setCreateTime(NormalTools.curDatetime());
                activityDao.save(act);
            } else { //修改
                Activity a = activityDao.findOne(act.getId());
                a.setContent(act.getContent());
                a.setTitle(act.getTitle());
                a.setStatus(act.getStatus());
                if(act.getPublishDate()!=null && !"".equals(act.getPublishDate())) {
                    a.setPublishDate(act.getPublishDate());
                } else {
                    a.setPublishDate(NormalTools.curDate());
                }
                a.setGuide(act.getGuide());
                if(act.getImgUrl()!=null && !"".equals(act.getImgUrl())) {
                    a.setImgUrl(act.getImgUrl());
                }
                activityDao.save(a);

                activityRecordDao.updateTitle(act.getId(), act.getTitle()); //要修改活动记录中的title
                activityApplyRecordDao.updateTitle(act.getId(), act.getTitle());
                activityCommentDao.updateTitle(act.getId(), act.getTitle());
            }
            return JsonResult.succ(act);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @AdminAuth(name = "删除", orderNum = 1)
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Activity a = activityDao.findOne(id);
            if(a.getCommentCount()>= 0 || a.getRecordCount()>0) {
                return JsonResult.error("有评论或活动开展记录，不可以删除");
            } else {
                activityDao.delete(id);
                return JsonResult.success("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
