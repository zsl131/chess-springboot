package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IActivityCommentDao;
import com.zslin.bus.basic.dao.IActivityDao;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityComment;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.model.WxAccount;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import com.zslin.bus.wx.tools.WxAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/8/2.
 */
@Service
@AdminAuth(name = "活动评论管理", psn = "系统管理", url = "/admin/activityComment", type = "1", orderNum = 4)
@HasTemplateMessage
public class ActivityCommentService {

    @Autowired
    private IActivityCommentDao activityCommentDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private WxAccountTools wxAccountTools;

    /*@Autowired
    private EventToolsThread eventToolsThread;*/

    @Autowired
    private TemplateMessageTools templateMessageTools;

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            ActivityComment ac = activityCommentDao.findOne(id);
            return JsonResult.succ(ac);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ActivityComment> res = activityCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), JsonParamTools.buildAuthDepSpe(params)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
    }

    @TemplateMessageAnnotation(name = "评论回复", keys = "评论日期-评论内容-回复内容")
    public JsonResult update(String params) {
        try {
            ActivityComment ac = JSON.toJavaObject(JSON.parseObject(params), ActivityComment.class);
            ActivityComment comment = activityCommentDao.findOne(ac.getId());
            comment.setReply(ac.getReply());
            comment.setReplyDate(NormalTools.curDate("yyyy-MM-dd"));
            comment.setReplyTime(NormalTools.curDatetime());
            comment.setStatus(ac.getStatus());

            activityCommentDao.save(comment);

            /*StringBuffer sb = new StringBuffer();
            sb.append("评论内容：").append(comment.getContent()).append("\\n")
                .append("回复内容：").append(comment.getReply());
            //TODO 需要告知评论者
            eventToolsThread.eventRemind(comment.getOpenid(), "您的评论已回复", "事件提醒", NormalTools.curDate(), sb.toString(), "/wx/activity/show?id="+comment.getActId());*/
            templateMessageTools.sendMessageByThread("评论回复", comment.getOpenid(), "#", "您的评论信息已得到回复", TemplateMessageTools.field("评论日期", comment.getCreateDate()), TemplateMessageTools.field("评论内容", comment.getContent()), TemplateMessageTools.field("回复内容", ac.getReply()));
            return JsonResult.success("回复成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult list4Wx(String params) {
        try {
            String openid = JsonTools.getJsonParam(params, "openid");
            Integer actId = Integer.parseInt(JsonTools.getJsonParam(params, "actId"));
            QueryListDto qld = QueryTools.buildQueryListDto(params);
            Page<ActivityComment> res = activityCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                    new SpecificationOperator("actId", "eq", actId),
                    new SpecificationOperator("status", "eq", "1", new SpecificationOperator("openid", "eq", openid, "or")) //状态为显示
//                    , new SpecificationOperator("openid", "eq", openid, "or") //暂时去掉此功能
                    ),
                    SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

            return JsonResult.getInstance().set("size", res.getTotalElements()).set("commentList", res.getContent()).set("totalPage", res.getTotalPages());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 当网友点赞 */
    public JsonResult onGood(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            activityCommentDao.updateGoodCount(id, 1);
            return JsonResult.success("点赞成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 修改状态 */
    public JsonResult updateStatus(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String status = JsonTools.getJsonParam(params, "status");
            activityCommentDao.updateStatus(id, status);
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @TemplateMessageAnnotation(name = "活动被评论提醒", keys = "评论日期-活动标题-评论内容")
    public JsonResult add(String params) {
        try {
            String openid = JsonTools.getJsonParam(params, "openid");
            String content = JsonTools.getJsonParam(params, "content");
            Integer objId = Integer.parseInt(JsonTools.getJsonParam(params, "objId"));

            Activity a = activityDao.findOne(objId);
            WxAccount account = wxAccountDao.findByOpenid(openid);
            ActivityComment ac = new ActivityComment();
            ac.setStatus("0");
            ac.setActId(objId);
            ac.setActTitle(a.getTitle());
            ac.setAvatarUrl(account.getAvatarUrl());
            ac.setContent(content);
            ac.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
            ac.setCreateLong(System.currentTimeMillis());
            ac.setCreateTime(NormalTools.curDatetime());
            ac.setGoodCount(0);
            ac.setNickname(account.getNickname());
            ac.setOpenid(openid);
            ac.setDepId(a.getDepId());
            ac.setDepName(a.getDepName());

            activityCommentDao.save(ac);

            activityDao.updateCommentCount(objId, 1);

            //TODO 需要通知管理员
            List<String> openids = wxAccountTools.getOpenid(WxAccountTools.ADMIN); //管理员Openid
//            eventToolsThread.eventRemind(openids, "活动被评论", "评论提醒", NormalTools.curDate(), "/wx/activity/show?id="+objId, new EventRemarkDto("评论内容：", content));
            templateMessageTools.sendMessageByThread("活动被评论提醒", openids, "#", "活动收到新评论", TemplateMessageTools.field("评论日期", ac.getCreateDate()), TemplateMessageTools.field("活动标题", ac.getActTitle()), TemplateMessageTools.field("评论内容", ac.getContent()));

            return JsonResult.success("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            ActivityComment ac = activityCommentDao.findOne(id);
            activityDao.updateCommentCount(ac.getActId(), -1); //修改活动评论次数
            activityCommentDao.delete(ac);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
