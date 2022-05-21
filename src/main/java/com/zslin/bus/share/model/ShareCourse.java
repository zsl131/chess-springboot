package com.zslin.bus.share.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 推广课程管理
 */
@Data
@Entity
@Table(name = "s_share_course")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ShareCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String courseName;

    private String startTime;

    /** 结束时间 */
    private String endTime;

    /**
     * 课程状态
     * 0 - 待上架； 1-可报名；2-已结束
     */
    private String status = "0";

    /** 报名原单价 */
    private Float price;

    /** 推广佣金 */
    private Float commissionMoney = 0f;

    /** 受推广后的优惠金额 */
    private Float discountMoney = 0f;

    @Lob
    private String content;
}
