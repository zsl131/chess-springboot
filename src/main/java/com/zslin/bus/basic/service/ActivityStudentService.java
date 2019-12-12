package com.zslin.bus.basic.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.dao.IActivityStudentDao;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.basic.model.ActivityStudent;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.rabbit.RabbitMQConfig;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.annotations.HasScore;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.ScoreAnnotation;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.tools.ScoreTools;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/8/10.
 */
@Service
@AdminAuth(name = "报名管理", psn = "系统管理", url = "/admin/activityStudent", type = "1", orderNum = 4)
@HasScore
@HasTemplateMessage
public class ActivityStudentService {

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ActivityStudent> res = activityStudentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), JsonParamTools.buildAuthDepSpe(params)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
    }

    /**
     * 微信端获取自己所报名的活动
     * @param params
     * @return
     */
    public JsonResult listOwn(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        String openid = JsonTools.getJsonParam(params, "openid");
        Page<ActivityStudent> res = activityStudentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("openid", "eq", openid)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 修改状态
     * @param params {"id":1, "status":"1", "reason":"通过"}
     * @return
     */
    @TemplateMessageAnnotation(name = "活动审核结果通知", keys = "活动主题-活动时间-审核结果")
    public JsonResult updateStatus(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String status = JsonTools.getJsonParam(params, "status");
            String reason = JsonTools.getJsonParam(params, "reason");
            ActivityStudent as = activityStudentDao.findOne(id);
            activityStudentDao.updateStatus(id, status, reason);
            String title = "";
            if("1".equalsIgnoreCase(status)) {title = "恭喜您，您的活动申请已通过";}
            else {title = "很遗憾，您的活动申请被驳回";}

            /*templateMessageTools.sendMessageByThread("活动审核结果通知", as.getOpenid(), "/wx/activityRecord/signUp?recordId="+as.getRecordId(), title,
                    TemplateMessageTools.field("活动主题", as.getActTitle()),
                    TemplateMessageTools.field("活动时间", as.getHoldTime()),
                    TemplateMessageTools.field("审核结果", "1".equals(status)?"通过":"驳回"),
                    TemplateMessageTools.field("1".equals(status)?("通过".equals(reason)?"":("注意事项："+reason+"若有事无法参与活动，请提前与我们联系，如一年内出现两次无故缺席将无法参与我们的活动。")):reason));*/

            SendMessageDto smd = new SendMessageDto("活动审核结果通知", as.getOpenid(), "/wx/activityRecord/signUp?recordId="+as.getRecordId(), title,
                    TemplateMessageTools.field("活动主题", as.getActTitle()),
                    TemplateMessageTools.field("活动时间", as.getHoldTime()),
                    TemplateMessageTools.field("审核结果", "1".equals(status)?"通过":"驳回"),
                    TemplateMessageTools.field("1".equals(status)?("通过".equals(reason)?"":("注意事项："+reason+"若有事无法参与活动，请提前与我们联系，如一年内出现两次无故缺席将无法参与我们的活动。")):reason));
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
            return JsonResult.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 用于签到
     * @param params {openid: 'abc123', recordId: 1}
     * @return
     */
    public JsonResult listRecordByOpenid(String params) {
        try {
            Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId"));
            String openid = JsonTools.getJsonParam(params, "openid");

            List<ActivityStudent> list = activityStudentDao.findByRecordIdAndOpenid(recordId, openid);
            ActivityRecord record = activityRecordDao.findOne(recordId);
            return JsonResult.succ(list).set("record", record);
        } catch (Exception e) {
            return JsonResult.error("出现错误啦");
        }
    }

    /**
     * 通过手机号码查找记录
     * @param params {phone: '1234123213', recordId: 1}
     * @return
     */
    public JsonResult listRecordByPhone(String params) {
        try {
            String phone = JsonTools.getJsonParam(params, "phone");
            Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId"));
            phone = phone.replace(" ", ""); //去空格
            //System.out.println("phone::"+phone+", recordId::"+recordId);

            List<ActivityStudent> list = activityStudentDao.findByRecordIdAndPhone(recordId, phone);
            return JsonResult.succ(list);
        } catch (Exception e) {
            return JsonResult.error("出现错误啦");
        }
    }

    /**
     * 签到
     * @param params {id}
     * @return
     */
    @ScoreAnnotation("活动签到得积分")
    @TemplateMessageAnnotation(name = "活动签到通知", keys = "学生姓名-活动名称-签到时间-活动地点")
    public JsonResult sign(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
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

            return JsonResult.success("签到成功");
        } catch (Exception e) {
            return JsonResult.error("出错了："+e.getMessage());
        }
    }
}
