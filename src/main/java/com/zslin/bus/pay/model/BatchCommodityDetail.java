package com.zslin.bus.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 活动具体内容
 */
@Entity
@Table(name = "p_batch_commodity_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchCommodityDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "batch_id")
    private Integer batchId;

    /** 批次号 */
    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @Column(name = "commodity_title")
    private String commodityTitle;

    /** 最多人数 */
    @Column(name = "total_amount")
    private Integer totalAmount=0;

    /** 当前报名人数 */
    @Column(name = "cur_amount")
    private Integer curAmount = 0;

    private Float price;

    /** 价值多少钱 */
    @Column(name = "worth_money")
    private Float worthMoney;
}
