package com.zslin.bus.pay.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 用于购买的产品
 */
@Entity
@Table(name="p_commodity")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    @Column(name = "pic_path")
    private String picPath;

    @Lob
    private String remark;

    @Column(name = "relation_activity_id")
    private Integer relationActivityId;

    @Column(name = "relation_activity_title")
    private String relationActivityTitle;

    @Column(name = "create_day")
    private String createDay;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;

    /** 活动报名次数 */
    @Column(name = "sign_count")
    private Integer signCount=0;

    /** 活动开展次数 */
    @Column(name = "dep_count")
    private Integer depCount=0;
}
