package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 年级管理
 * Created by zsl on 2018/12/14.
 */
@Entity
@Table(name = "y_grade")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    /** 序号 */
    @Column(name = "order_no")
    private Integer orderNo;

    /** 是否供教师选择 */
    @Column(name = "teacher_flag")
    private String teacherFlag;

    /** 关联的体系ID */
    private Integer sid;

    /** 关联的体系名称 */
    private String sname;

    private String remark;

    public String getTeacherFlag() {
        return teacherFlag;
    }

    public void setTeacherFlag(String teacherFlag) {
        this.teacherFlag = teacherFlag;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
