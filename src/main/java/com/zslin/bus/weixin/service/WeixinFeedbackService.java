package com.zslin.bus.weixin.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dao.IFeedbackDao;
import com.zslin.bus.wx.model.Feedback;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/10/11.
 */
@Service
@HasTemplateMessage
public class WeixinFeedbackService {

    @Autowired
    private IFeedbackDao feedbackDao;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    /**
     * 获取反馈数据
     * @param params {openid:'11'}
     * @return
     */
    public JsonResult listFeedback(String params) {
        String openid = JsonTools.getJsonParam(params, "openid");
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Feedback> res = feedbackDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("openid", "eq", openid) //活动详情Id
//                    , new SpecificationOperator("openid", "eq", openid, "or") //或openid为当前用户， 暂时去掉此功能
                ),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.getInstance().set("totalCount", res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 获取所有
     * @param params
     * @return
     */
    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Feedback> res = feedbackDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()
//                    , new SpecificationOperator("openid", "eq", openid, "or") //或openid为当前用户， 暂时去掉此功能
                ),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.getInstance().set("totalCount", res.getTotalElements()).set("data", res.getContent()).set("totalPage", res.getTotalPages());
    }

    public JsonResult loadOne(String params) {
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        Feedback feedback = feedbackDao.findOne(id);
        return JsonResult.succ(feedback);
    }

    //name = "评论回复", keys = "评论日期-评论内容-回复内容"
    @TemplateMessageAnnotation(name = "反馈回复", keys = "反馈日期-反馈内容-回复内容")
    public JsonResult reply(String params) {
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        String reply = JsonTools.getJsonParam(params, "reply");
        Feedback f = feedbackDao.findOne(id);
        f.setReply(reply);
        f.setReplyLong(System.currentTimeMillis());
        f.setReplyDate(NormalTools.curDate("yyyy-MM-dd"));
        f.setReplyTime(NormalTools.curDatetime());

        feedbackDao.save(f);

        /*templateMessageTools.sendMessageByThread("反馈回复", f.getOpenid(), "#", "您的反馈信息已得到回复", TemplateMessageTools.field("反馈日期", f.getCreateDate()),
                TemplateMessageTools.field("反馈内容", f.getContent()), TemplateMessageTools.field("回复内容", f.getReply()));*/

        templateMessageTools.sendMessageByThread("反馈回复", f.getOpenid(), "#", "您的反馈信息已得到回复", TemplateMessageTools.field("反馈日期", f.getCreateDate()),
                TemplateMessageTools.field("反馈内容", f.getContent()), TemplateMessageTools.field("回复内容", reply));
        return JsonResult.success("回复成功");
    }
}
