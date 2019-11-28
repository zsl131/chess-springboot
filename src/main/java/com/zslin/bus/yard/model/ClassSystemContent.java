package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/9/12.
 * 课程体系内容
 */
@Entity
@Table(name = "y_class_system_content")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassSystemContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 年级 */
    private String grade;

    /** 学期，上下 */
    private String term;

    /** 序号，属于第几次课 */
    @Column(name = "order_no")
    private Integer orderNo;

    /** 体系ID */
    @Column(name = "sys_id")
    private Integer sysId;

    /** 体系名称 */
    @Column(name = "sys_name")
    private String sysName;

    /** 课程ID */
    @Column(name = "course_id")
    private Integer courseId;

    /** 课程名称 */
    @Column(name = "course_title")
    private String courseTitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getSysId() {
        return sysId;
    }

    public void setSysId(Integer sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
