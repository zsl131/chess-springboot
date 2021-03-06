package com.zslin.bus.pay.dto;

import lombok.Data;

/**
 * 提交订单的DTO对象
 */
@Data
public class SubmitOrdersDto {

    private String ordersNo;

    private Float price;

    private Integer stuCount;

    private String stuNames;

    private String actStuIds;

    private String bodyTitle;

    private String ip;
}
