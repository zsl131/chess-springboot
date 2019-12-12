package com.zslin.bus.finance.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.dao.IUserDao;
import com.zslin.basic.model.User;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.rabbit.RabbitMQConfig;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.finance.dao.IFinanceDetailDao;
import com.zslin.bus.finance.dao.IFinanceRecordDao;
import com.zslin.bus.finance.dao.IFinanceTicketDao;
import com.zslin.bus.finance.dto.NoDto;
import com.zslin.bus.finance.model.FinanceDetail;
import com.zslin.bus.finance.model.FinanceRecord;
import com.zslin.bus.finance.model.FinanceTicket;
import com.zslin.bus.finance.tools.MoneyTools;
import com.zslin.bus.finance.tools.TicketNoTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2019/1/10.
 */
@Service
@AdminAuth(name = "账务登记", psn = "财务管理", url = "/admin/financeRecord", type = "1", orderNum = 1)
@HasTemplateMessage
public class FinanceRecordService {

    @Autowired
    private IFinanceRecordDao financeRecordDao;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IFinanceTicketDao financeTicketDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private TicketNoTools ticketNoTools;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<FinanceRecord> res = financeRecordDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        Float totalIn = financeRecordDao.sum("1");
        Float totalOut = financeRecordDao.sum("-1");
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent())
                .set("totalIn", totalIn).set("totalOut", totalOut);
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        FinanceRecord record = financeRecordDao.findOne(id);
        List<FinanceDetail> detailList = financeDetailDao.findByTicketNo(record.getTicketNo());
        List<FinanceTicket> ticketList = financeTicketDao.findByTicketNo(record.getTicketNo());
        return JsonResult.success().set("record", record).set("detailList", detailList)
                .set("ticketList", ticketList)
                .set("chineseMoney", MoneyTools.digitUppercase(record.getAmount()));
    }

    @AdminAuth(name = "账目审核", orderNum = 2)
    @TemplateMessageAnnotation(name = "对账单通知", keys = "对账单号-帐单名称-消费笔数-消费金额-生成时间")
    public JsonResult updateStatus(String params) {
        String status = JsonTools.getJsonParam(params, "status");
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        String reason = JsonTools.getJsonParam(params, "reason");
        String name = "", phone = "";
        try {
            String username = getUsername(params);
            User u = userDao.findByUsername(username);
            name = u.getUsername()+"-"+u.getNickname();
            phone = u.getPhone();
        } catch (Exception e) {
        }
        FinanceRecord fr = financeRecordDao.findOne(id);
        String verifyTime = NormalTools.curDatetime();
        if("1".equals(status)) {
            financeRecordDao.updateStatusByPass("1", name, verifyTime, id);
            //审核通过后发送财务通知
            /*templateMessageTools.sendMessageByThread("对账单通知", buildFinanceOpenids(), "#", "有财务账单需要处理",
                    TemplateMessageTools.field("对账单号", fr.getTicketNo()),
                    TemplateMessageTools.field("帐单名称", fr.getRecordName()),
                    TemplateMessageTools.field("消费笔数", fr.getDetailCount() + " 笔"),
                    TemplateMessageTools.field("消费金额", fr.getAmount()+" 元"),
                    TemplateMessageTools.field("生成时间", fr.getCreateTime()),
                    TemplateMessageTools.field("请尽快核对处理"));*/

            SendMessageDto smd = new SendMessageDto("对账单通知", buildFinanceOpenids(), "#", "有财务账单需要处理",
                    TemplateMessageTools.field("对账单号", fr.getTicketNo()),
                    TemplateMessageTools.field("帐单名称", fr.getRecordName()),
                    TemplateMessageTools.field("消费笔数", fr.getDetailCount() + " 笔"),
                    TemplateMessageTools.field("消费金额", fr.getAmount()+" 元"),
                    TemplateMessageTools.field("生成时间", fr.getCreateTime()),
                    TemplateMessageTools.field("请尽快核对处理"));
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
        } else {
            financeRecordDao.updateStatusByInvalid("-1", reason, name, phone, verifyTime, id);
        }
        financeDetailDao.updateStatus(status, reason, name, phone, fr.getTicketNo());
        return JsonResult.success("操作成功");
    }

    private List<String> buildFinanceOpenids() {
        List<String> res = new ArrayList<>();
        res.add("o4Jhl0sqcFLtEnpFs27wSc5xVgLg"); //孔浩
        res.add("o4Jhl0nkSmiRIqU9JkWIm2lj6qXE"); //钟述林
        return res;
    }

    private List<String> buildVerifyOpenids() {
        List<String> res = new ArrayList<>();
        res.add("o4Jhl0hQe6hqTzk1Sfb0nuxT10cE"); //孔浩
        res.add("o4Jhl0nkSmiRIqU9JkWIm2lj6qXE"); //钟述林
        return res;
    }

    @AdminAuth(name = "账目审核", orderNum = 2)
    @TemplateMessageAnnotation(name = "对账单通知", keys = "对账单号-帐单名称-消费笔数-消费金额-生成时间")
    public JsonResult save(String params) {
        String flag = JsonTools.getJsonParam(params, "flag");
        String details = JsonTools.getJsonParam(params, "details");
        JSONArray detailArray = JsonTools.str2JsonArray(details);
        String createDate = NormalTools.curDate();
        String createTime = NormalTools.curDatetime();
        Long createLong = System.currentTimeMillis();
        String createMonth = createDate.replaceAll("-", "").substring(0, 6);
        NoDto noDto = ticketNoTools.getNewRecordTicketNo(createMonth);
        User u = null;
        try {
            String username = getUsername(params);
            u = userDao.findByUsername(username);
        } catch (Exception e) {
        }
        Float totalAmount = 0f;
        Integer totalCount = detailArray.length();
        Integer ticketCount = 0;
        for(int i=0;i<detailArray.length();i++) {
            JSONObject jsonObj = detailArray.getJSONObject(i);
            JSONArray picArray = jsonObj.getJSONArray("tickets"); //图片
            Integer cateId = jsonObj.getInt("cateId"); //分类ID
            String cateName = jsonObj.getString("cateName");
            Float price = Float.parseFloat(jsonObj.get("price").toString());
            Integer count = jsonObj.getInt("count");
            String recordDate = jsonObj.getString("recordDate").replaceAll("-", "");
            String title = jsonObj.getString("title");
            FinanceDetail fd = new FinanceDetail();
            fd.setRecordDate(recordDate);
            fd.setTicketCount(picArray.length());
            fd.setAmount(price * count);
            fd.setCateName(cateName);
            fd.setCateId(cateId);
            fd.setCount(count);
            fd.setFlag(flag);
            fd.setPrice(price);
            fd.setRecordMonth(recordDate.substring(0, 6));
            fd.setRecordYear(recordDate.substring(0, 4));
            fd.setTitle(title);
            fd.setCreateDate(createDate);
            fd.setCreateTime(createTime);
            fd.setCreateLong(createLong);
            fd.setTicketNo(noDto.getNo());
            fd.setStatus("0");
            fd.setTno(noDto.getTno());
            if(u!=null) {
                fd.setRecordName(u.getUsername()+"-"+u.getNickname());
                fd.setOperator(u.getUsername()+"-"+u.getNickname());
                fd.setRecordPhone(u.getPhone());
            }
            ticketCount += fd.getTicketCount();
            financeDetailDao.save(fd);
            totalAmount += fd.getAmount(); //合计金额
            for(int j=0;j<picArray.length();j++) {
                FinanceTicket ft = new FinanceTicket();
                ft.setDetailId(fd.getId());
                ft.setTicketNo(noDto.getNo());
                ft.setPicUrl(picArray.get(j).toString());
                financeTicketDao.save(ft);
            }
        }
        FinanceRecord fr = new FinanceRecord();
        fr.setTicketNo(noDto.getNo());
        fr.setTno(noDto.getTno());
        if(u!=null) {
            fr.setRecordName(u.getUsername()+"-"+u.getNickname());
            fr.setOperator(u.getUsername()+"-"+u.getNickname());
            fr.setRecordPhone(u.getPhone());
        }
        fr.setFlag(flag);
        fr.setAmount(totalAmount);
        fr.setDetailCount(totalCount);
        fr.setCreateDate(createDate);
        fr.setCreateTime(createTime);
        fr.setCreateLong(createLong);
        fr.setRecordMonth(createMonth);
        fr.setRecordDate(createDate.replaceAll("-", ""));
        fr.setRecordYear(createDate.substring(0, 4));
        fr.setTicketCount(ticketCount);
        fr.setStatus("0"); //待审核
        financeRecordDao.save(fr);

        //添加时发送财务通知请求审核
        /*templateMessageTools.sendMessageByThread("对账单通知", buildVerifyOpenids(), "#", "有财务账单需要审核",
                TemplateMessageTools.field("对账单号", fr.getTicketNo()),
                TemplateMessageTools.field("帐单名称", fr.getRecordName()),
                TemplateMessageTools.field("消费笔数", fr.getDetailCount() + " 笔"),
                TemplateMessageTools.field("消费金额", fr.getAmount()+" 元"),
                TemplateMessageTools.field("生成时间", fr.getCreateTime()),
                TemplateMessageTools.field("请尽快登陆后台审核"));*/

        SendMessageDto smd = new SendMessageDto("对账单通知", buildVerifyOpenids(), "#", "有财务账单需要审核",
                TemplateMessageTools.field("对账单号", fr.getTicketNo()),
                TemplateMessageTools.field("帐单名称", fr.getRecordName()),
                TemplateMessageTools.field("消费笔数", fr.getDetailCount() + " 笔"),
                TemplateMessageTools.field("消费金额", fr.getAmount()+" 元"),
                TemplateMessageTools.field("生成时间", fr.getCreateTime()),
                TemplateMessageTools.field("请尽快登陆后台审核"));
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);

        return JsonResult.success("保存成功");
    }

    private String getUsername(String params) {
        try {
            String headerParams = JsonTools.getJsonParam(params, "headerParams");
            String username = JsonTools.getJsonParam(headerParams, "username");
            return username;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
