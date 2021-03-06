package com.zslin.bus.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "p_orders")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 订单编号 */
    private String ordersNo;

    /** 学生报名记录ID，如：1,3,6 */
    private String actStuIds;

    private String stuNames;

    private Integer stuCount = 0;

    private Float price=0f;

    private Float totalMoney=0f;

    private Integer accountId;

    private String openid;

    private String nickname;

    private String headImgUrl;

    private String payDate;

    private String payTime;

    private Long payLong;

    private String createDate;

    private String createTime;

    private Long createLong;

    private String payFlag = "0";

    private String status;

    /** 用于支付时显示的标题 */
    private String bodyTitle;
}
