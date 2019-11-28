package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 用户积分规则管理系统
 */
@Entity
@Table(name="t_score_rule")
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class ScoreRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 名称 */
    private String name;

    /** SN */
    private String sn;

    /** 所加分值 */
    private Integer score;

    /** 每个用户总的可以加分几次，如果小于0，则无限次数 */
    @Column(name = "total_amount")
    private Integer totalAmount;

    /** 每天单个用户可加分次数，如果小于0，则不限次数 */
    @Column(name = "day_amount")
    private Integer dayAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(Integer dayAmount) {
        this.dayAmount = dayAmount;
    }
}
