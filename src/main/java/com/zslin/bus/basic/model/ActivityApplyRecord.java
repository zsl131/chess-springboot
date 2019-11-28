package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/7/23.
 * 活动报名记录
 */
@Entity
@Table(name = "t_activity_apply_record")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ActivityApplyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 活动详情ID */
    @Column(name = "act_id")
    private Integer actId;

    /** 活动详情标题 */
    @Column(name = "act_title")
    private String actTitle;

    /** 活动记录ID */
    @Column(name = "act_record_id")
    private Integer actRecordId;

    private String openid;

    @Column(name = "account_id")
    private Integer accountId;

    private String nickname;

    /** 家长电话 */
    private String phone;

    /** 学生姓名 */
    @Column(name = "stu_name")
    private String stuName;

    /** 学生性别，1-男，2-女 */
    private String sex;

    @Column(name = "age_id")
    private Integer ageId;

    @Column(name = "age_name")
    private String ageName;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "school_name")
    private String schoolName;

    /** 状态，0-未审核；1-审核通过，可以参与活动；2-被驳回，不允许参加活动 */
    private String status;

    /** 审核原因，驳回必须有原因 */
    private String reason;

    /** 申请日期，yyyy-MM-dd */
    @Column(name = "apply_date")
    private String applyDate;

    /** 申请时间，yyyy-MM-dd HH:mm:ss */
    @Column(name = "apply_time")
    private String applyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public Integer getActRecordId() {
        return actRecordId;
    }

    public void setActRecordId(Integer actRecordId) {
        this.actRecordId = actRecordId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAgeId() {
        return ageId;
    }

    public void setAgeId(Integer ageId) {
        this.ageId = ageId;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }
}
