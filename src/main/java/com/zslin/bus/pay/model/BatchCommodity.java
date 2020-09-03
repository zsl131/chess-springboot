package com.zslin.bus.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "p_batch_commodity")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchCommodity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 批次号 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 开展时间 */
    @Column(name = "dep_time")
    private String depTime;

    /** 开展地址 */
    @Column(name = "dep_address")
    private String depAddress;

    /** 所需总人数 */
    @Column(name = "total_amount")
    private Integer totalAmount = 0;

    /** 当前报名人数 */
    private Integer curAmount = 0;
}
