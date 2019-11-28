package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.INoticeCommentDao;
import com.zslin.bus.basic.dao.INoticeDao;
import com.zslin.bus.basic.model.NoticeComment;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import com.zslin.bus.wx.tools.WxAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/22.
 */
@Service
@AdminAuth(name = "通知公告评论管理", psn = "系统管理", type = "1", url = "/admin/noticeComment", orderNum = 11)
@HasTemplateMessage
public class NoticeCommentService {

    @Autowired
    private INoticeCommentDao noticeCommentDao;

    @Autowired
    private INoticeDao noticeDao;

    @Autowired
    private WxAccountTools wxAccountTools;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<NoticeComment> res = noticeCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        JsonResult result =  JsonResult.getInstance().set("size", (int) res.getTotalElements())
                .set("data", res.getContent());

        return result;
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            NoticeComment ac = noticeCommentDao.findOne(id);
            return JsonResult.succ(ac);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @TemplateMessageAnnotation(name = "评论回复", keys = "评论日期-评论内容-回复内容")
    public JsonResult update(String params) {
        try {
            NoticeComment ac = JSON.toJavaObject(JSON.parseObject(params), NoticeComment.class);
            NoticeComment comment = noticeCommentDao.findOne(ac.getId());
            comment.setReply(ac.getReply());
            comment.setReplyDate(NormalTools.curDate("yyyy-MM-dd"));
            comment.setReplyTime(NormalTools.curDatetime());
            comment.setStatus(ac.getStatus());

            noticeCommentDao.save(comment);

            StringBuffer sb = new StringBuffer();
            sb.append("评论内容：").append(comment.getContent()).append("\\n")
                    .append("回复内容：").append(comment.getReply());
            //TODO 需要告知评论者
            templateMessageTools.sendMessageByThread("评论回复", comment.getOpenid(), "#", "您的评论信息已得到回复", TemplateMessageTools.field("评论日期", comment.getCreateDay()), TemplateMessageTools.field("评论内容", comment.getContent()), TemplateMessageTools.field("回复内容", ac.getReply()));
            return JsonResult.success("回复成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @TemplateMessageAnnotation(name = "文章被评论提醒", keys = "评论日期-文章标题-评论内容")
    public JsonResult add(String params) {
        try {
            NoticeComment obj = JSONObject.toJavaObject(JSON.parseObject(params), NoticeComment.class);
            obj.setCreateDay(NormalTools.curDate());
            obj.setCreateTime(NormalTools.curDatetime());
            obj.setCreateLong(System.currentTimeMillis());
            noticeCommentDao.save(obj);
            noticeDao.updateCommentCount(obj.getNoticeId(), 1); //让评论数加1

            //TODO 需要通知管理员
            List<String> openids = wxAccountTools.getOpenid(WxAccountTools.ADMIN); //管理员Openid
//            eventToolsThread.eventRemind(openids, "活动被评论", "评论提醒", NormalTools.curDate(), "/wx/activity/show?id="+objId, new EventRemarkDto("评论内容：", content));
            templateMessageTools.sendMessageByThread("文章被评论提醒", openids, "#", "文章收到新评论", TemplateMessageTools.field("评论日期", obj.getCreateDay()), TemplateMessageTools.field("文章标题", obj.getNoticeTitle()), TemplateMessageTools.field("评论内容", obj.getContent()));
            return JsonResult.succ(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult list4Wx(String params) {
        try {
            String openid = JsonTools.getJsonParam(params, "openid");
            Integer actId = Integer.parseInt(JsonTools.getJsonParam(params, "noticeId"));
            QueryListDto qld = QueryTools.buildQueryListDto(params);
            Page<NoticeComment> res = noticeCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                    new SpecificationOperator("noticeId", "eq", actId),
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
            noticeCommentDao.updateGoodCount(id, 1);
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
            noticeCommentDao.updateStatus(id, status);
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            NoticeComment ac = noticeCommentDao.findOne(id);
            noticeDao.updateCommentCount(ac.getNoticeId(), -1); //修改活动评论次数
            noticeCommentDao.delete(ac);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
