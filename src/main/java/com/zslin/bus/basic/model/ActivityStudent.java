package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/8/7.
 * 学生报名参加活动
 */
@Entity
@Table(name = "t_activity_student")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ActivityStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String openid;

    @Column(name = "act_id")
    private Integer actId;

    @Column(name = "act_title")
    private String actTitle;

    @Column(name = "record_id")
    private Integer recordId;

    /** 活动开展时间 */
    @Column(name = "hold_time")
    private String holdTime;

    @Column(name = "stu_id")
    private Integer stuId;

    @Column(name = "stu_name")
    private String stuName;

    @Column(name = "dep_id")
    private Integer depId;

    @Column(name = "dep_name")
    private String depName;

    /**
     * 状态，0-提交申请，未审核；1-审核通过；2-被驳回
     */
    private String status;

    /** 驳回原因 */
    @Column(name = "reject_reason")
    private String rejectReason;

    private String sex;

    @Column(name = "age_id")
    private Integer ageId;

    @Column(name = "age_name")
    private String ageName;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "school_name")
    private String schoolName;

    private String phone;

    /** 活动地点 */
    private String address;

    /** 是否签到 */
    @Column(name = "has_check")
    private String hasCheck = "0";

    /** 创建时间 */
    @Column(name = "create_time")
    private String createTime;

    /** 报名来源，0-社会；1-学校 */
    @Column(name = "from_flag")
    private String fromFlag = "0";

    /** 是否缴费 */
    @Column(name = "pay_flag")
    private String payFlag = "0";

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public String getFromFlag() {
        return fromFlag;
    }

    public void setFromFlag(String fromFlag) {
        this.fromFlag = fromFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHasCheck() {
        return hasCheck;
    }

    public void setHasCheck(String hasCheck) {
        this.hasCheck = hasCheck;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
