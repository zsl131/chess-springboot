package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 课程体系详情
 * Created by zsl on 2018/12/22.
 */
@Entity
@Table(name = "y_class_system_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassSystemDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** System ID */
    private Integer sid;

    /** System Name */
    private String sname;

    /** System Parent ID */
    private Integer spid;

    /** System Parent Name */
    private String spname;

    private String name;

    @Column(name = "order_no")
    private Integer orderNo;

    /** 章节序号，如：5.1 */
    @Column(name = "section_no")
    private Float sectionNo;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_title")
    private String courseTitle;

    /** 课程学习目标 */
    @Column(name = "course_target")
    @Lob
    private String courseTarget;

    /** Category Name */
    private String cname;

    /** Category Parent Name */
    private String cpname;

    @Column(name = "in_range")
    private String inRange;

    public String getInRange() {
        return inRange;
    }

    public void setInRange(String inRange) {
        this.inRange = inRange;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
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

    public String getCourseTarget() {
        return courseTarget;
    }

    public void setCourseTarget(String courseTarget) {
        this.courseTarget = courseTarget;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }

    public String getSpname() {
        return spname;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Float getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(Float sectionNo) {
        this.sectionNo = sectionNo;
    }
}
