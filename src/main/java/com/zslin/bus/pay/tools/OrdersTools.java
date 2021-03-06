package com.zslin.bus.pay.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.pay.dao.IOrdersDao;
import com.zslin.bus.pay.dto.SubmitOrdersDto;
import com.zslin.bus.pay.model.Orders;
import com.zslin.bus.wx.model.WxAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单处理工具
 */
@Component("ordersTools")
public class OrdersTools {

    @Autowired
    private IOrdersDao ordersDao;

    @Autowired
    private PayTools payTools;

    private void addNewOrders(WxAccount account, SubmitOrdersDto objDto) {
        //String ordersNo, Float price, Integer stuCount,
        //                           String stuNames, String actStuIds, String bodyTitle
        Orders orders = new Orders();
        orders.setPayFlag("0");
        orders.setStatus("0");
        orders.setAccountId(account.getId());
        orders.setCreateDate(NormalTools.curDate());
        orders.setCreateLong(System.currentTimeMillis());
        orders.setCreateTime(NormalTools.curDatetime());
        orders.setHeadImgUrl(account.getAvatarUrl());
        orders.setNickname(account.getNickname());
        orders.setOpenid(account.getOpenid());
        orders.setOrdersNo(objDto.getOrdersNo());
        orders.setPrice(objDto.getPrice());
        orders.setStuCount(objDto.getStuCount());
        orders.setStuNames(objDto.getStuNames());
        orders.setActStuIds(objDto.getActStuIds());
        orders.setTotalMoney(objDto.getPrice()*objDto.getStuCount());
        orders.setBodyTitle(objDto.getBodyTitle());

        //1. 保存到数据库
        ordersDao.save(orders);
        //2. 向微信支付接口发起统一下单
        payTools.unifiedOrder(objDto.getIp(), objDto.getOrdersNo());
    }
}
