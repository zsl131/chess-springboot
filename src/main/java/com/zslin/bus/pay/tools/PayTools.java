package com.zslin.bus.pay.tools;

import com.github.wxpay.sdk.MyPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.tools.RandomTools;
import com.zslin.bus.pay.dao.IOrdersDao;
import com.zslin.bus.pay.dao.IUnifiedOrderDao;
import com.zslin.bus.pay.dto.PaySubmitDto;
import com.zslin.bus.pay.model.Orders;
import com.zslin.bus.pay.model.UnifiedOrder;
import com.zslin.bus.wx.model.WxConfig;
import com.zslin.bus.wx.tools.WxConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PayTools {

    @Autowired
    private WxConfigTools wxConfigTools;

    @Autowired
    private IOrdersDao ordersDao;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IUnifiedOrderDao unifiedOrderDao;

    /**
     * 统一下单接口
     * @return 返回一个可以直接到小程序进行支付的DTO对象
     */
    public PaySubmitDto unifiedOrder(String ip, String ordersNo) {
        UnifiedOrder resOrder = new UnifiedOrder();
        //Map resultMap=new HashMap();
        Orders orders = ordersDao.findByOrdersNo(ordersNo);
        //获取微信小程序配置文件
        WxConfig config = wxConfigTools.getWxConfig();
        Map<String, String> data = new HashMap<>();

        String appId = config.getAppid();
        String apiKey = config.getApiKey();

        String nonceStr = RandomTools.randomString(32);
        String body;
        String proTitles = orders.getStuNames();
        if(proTitles==null || "".equals(proTitles.trim())) {
            body = "活动报名费用"; //支付名称
        } else {body = proTitles;}
        Float money = orders.getTotalMoney();
        String sign = PayUtils.buildSign(appId, config.getMchid(), body, apiKey, nonceStr);
        data.put("appid", appId);
        data.put("mch_id", config.getMchid());
        data.put("nonce_str", nonceStr);
        data.put("body", body);
        data.put("out_trade_no",ordersNo);
        data.put("total_fee", buildTotalMoney(money));
        data.put("spbill_create_ip", ip);
        data.put("notify_url", config.getPayNotifyUrl()); //支付结果通知地址
        data.put("trade_type","JSAPI"); //交易类型，小程序填：JSAPI
        data.put("openid", orders.getOpenid());
        data.put("sign", sign);

        resOrder.setPayMoney(orders.getTotalMoney()); //支付金额
        resOrder.setAccountId(orders.getAccountId());
        resOrder.setHeadImgUrl(orders.getHeadImgUrl());
        resOrder.setNickname(orders.getNickname());
        resOrder.setOpenid(orders.getOpenid());
        resOrder.setOrdersId(orders.getId());
        resOrder.setOrdersNo(orders.getOrdersNo());

        try {

            String certPath = configTools.getUploadPath("cert") + "apiclient_cert.p12";

            MyPayConfig payConfig = new MyPayConfig(certPath, config);
            WXPay wxpay = new WXPay(payConfig);

            Map<String, String> rMap = wxpay.unifiedOrder(data);
            //System.out.println("统一下单接口返回: " + rMap);
            //log.info(rMap.toString()); //显示结果
            //  err_code=ORDERPAID, return_msg=OK, result_code=FAIL, err_code_des=??????
            String return_code = rMap.get("return_code");
            String result_code = rMap.get("result_code");
            String err_code = rMap.get("err_code");
            String err_code_des = rMap.get("err_code_des");
            resOrder.setErrCode(err_code);
            resOrder.setErrCodeDes(err_code_des);
            /*resultMap.put("nonceStr", nonceStr);*/
            //Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id"); //预支付订单ID

                resOrder.setPrepayId(prepayid);
                resOrder.setStatus("0"); //表示获取成功
            } else if("ORDERPAID".equalsIgnoreCase(err_code)) { //如果是支付，则修改订单状态
                resOrder.setStatus("0");
                hasPayed(orders); //
            } else {
//                return  response;
                resOrder.setStatus("-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            return  response;
            resOrder.setStatus("-2");
        }

        //String status = resOrder.getStatus();
//        log.info(resOrder.toString());
        resOrder.setCreateDay(NormalTools.curDate());
        resOrder.setCreateTime(NormalTools.curDatetime());
        resOrder.setCreateLong(System.currentTimeMillis());
        unifiedOrderDao.save(resOrder); //存入数据库
        //在没有出错且prepayId存在时返回DTO
        return buildSubmitData(appId, apiKey, resOrder);
    }

    public void hasPayed(Orders orders) {
        //String ordersNo = orders.getOrdersNo();
        orders.setStatus("1");
        orders.setPayFlag("1");
        String payDay = NormalTools.curDate();
        String payTime = NormalTools.curDatetime();
        Long payLong = System.currentTimeMillis();
        orders.setPayTime(payTime);
        orders.setPayDate(payDay);
        orders.setPayLong(payLong);
        ordersDao.save(orders);
    }

    /**
     * 生成调起支付的DTO对象
     * @return
     */
    private PaySubmitDto buildSubmitData(String appId, String apiKey, UnifiedOrder unifiedOrder) {
        String prepayId = unifiedOrder.getPrepayId();
        String status = unifiedOrder.getStatus();
        String nonceStr = RandomTools.randomString(32);
        String timestamp = (System.currentTimeMillis() / 1000)+"";
        String sign = PayUtils.buildPaySign(appId, nonceStr, prepayId, timestamp, apiKey);

        PaySubmitDto dto = new PaySubmitDto();
        dto.setAppId(appId);
        dto.setNonceStr(nonceStr);
        dto.setPackageStr("prepay_id="+prepayId);
        dto.setTimeStamp(timestamp);
        dto.setSignType("MD5");
        dto.setPaySign(sign);

        if("0".equals(status) && prepayId!=null && !"".equals(prepayId)) {
            dto.setFlag("1");
        } else {dto.setFlag("0");}
        dto.setUnifiedOrder(unifiedOrder);

        //log.info(dto.toString());
        return dto;
    }

    /** 把订单金额换成分 */
    private String buildTotalMoney(Float totalMoney) {
        return String.valueOf((int)(totalMoney*100));
    }
}
