package com.zslin.bus.wx.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.rabbit.RabbitMQConfig;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dao.IFeedbackDao;
import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.model.Feedback;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/21.
 */
@Service
@AdminAuth(name = "反馈管理", psn = "微信管理", orderNum = 5, type = "1", url = "/admin/feedback")
@HasTemplateMessage
public class FeedbackService {

    @Autowired
    private IFeedbackDao feedbackDao;

//    @Autowired
//    private EventToolsThread eventToolsThread;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Feedback> res = feedbackDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("datas", res.getContent());
    }

    public JsonResult updateStatus(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String status = JsonTools.getJsonParam(params, "status");
            feedbackDao.updateStatus(id, status);
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Feedback f = feedbackDao.findOne(id);
            return JsonResult.succ(f);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 回复
     * @param params {id:1, reply: 回复}
     * @return
     */
    @TemplateMessageAnnotation(name = "反馈回复", keys = "反馈日期-反馈内容-回复内容")
    public JsonResult reply(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String reply = JsonTools.getJsonParam(params, "reply");
            Feedback f = feedbackDao.findOne(id);
            f.setReply(reply);
            f.setReplyDate(DateTools.currentDay());
            f.setReplyTime(DateTools.currentDay("yyyy-MM-dd HH:mm:ss"));
            f.setReplyLong(System.currentTimeMillis());
            feedbackDao.save(f);

            //TODO 通知反馈者
//            boolean result = eventToolsThread.eventRemind(f.getOpenid(), "回复反馈啦", "反馈回复", NormalTools.curDate(), reply, "#");
//            templateMessageTools.sendMessageByThread("反馈回复", f.getOpenid(), "#", "您的反馈信息已得到回复", TemplateMessageTools.field("反馈日期", f.getCreateDate()), TemplateMessageTools.field("反馈内容", f.getContent()), TemplateMessageTools.field("回复内容", reply));

            SendMessageDto smd = new SendMessageDto("反馈回复", f.getOpenid(), "#", "您的反馈信息已得到回复", TemplateMessageTools.field("反馈日期", f.getCreateDate()), TemplateMessageTools.field("反馈内容", f.getContent()), TemplateMessageTools.field("回复内容", reply));
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);

            return JsonResult.succ(f);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
