package com.zslin.bus.wx.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zsl on 2018/7/3.
 */
@Entity
@Table(name = "wx_account")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WxAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String openid;

    private String nickname;

    /** 头像 */
    @Column(name = "avatar_url")
    private String avatarUrl;

    private String phone;

    private String name;

    private String sex;

    private String identity;

    @Column(name = "bind_phone")
    private String bindPhone="0";

    /** 状态：0-取消关注；1-关注中 */
    private String status;

    /**
     * 类型
     * 0-游客；
     * 1-学生；
     * 2-学生家长；
     * 4-科普教师
     * 5-公司员工；
     * 10-超级管理人员
     */
    private String type;

    /**
     * 被拉取用户的id，即是被哪个用户拉进来的
     */
    @Column(name = "follow_user_id")
    private Integer followUserId=0;

    /**
     * 被摘取用户的昵称，即是被哪个用户摘取进来的
     */
    @Column(name = "follow_user_name")
    private String followUserName;

    /** 关注日期,yyyy-MM-dd */
    @Column(name = "follow_date")
    private String followDate;

    /** 关注日期，yyyy-MM-dd HH:mm:ss */
    @Column(name = "follow_datetime")
    private String followDatetime;

    /** 取消关注日期，yyyy-MM-dd */
    @Column(name = "cancel_date")
    private String cancelDate;

    /** 取消关注日期，yyyy-MM-dd HH:mm:ss */
    @Column(name = "cancel_datetime")
    private String cancelDatetime;

    @Column(name = "create_long")
    private Long createLong;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "create_time")
    private String createTime;

    /** 积分 */
    private Integer score = 0;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(Integer followUserId) {
        this.followUserId = followUserId;
    }

    public String getFollowUserName() {
        return followUserName;
    }

    public void setFollowUserName(String followUserName) {
        this.followUserName = followUserName;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public String getFollowDatetime() {
        return followDatetime;
    }

    public void setFollowDatetime(String followDatetime) {
        this.followDatetime = followDatetime;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getCancelDatetime() {
        return cancelDatetime;
    }

    public void setCancelDatetime(String cancelDatetime) {
        this.cancelDatetime = cancelDatetime;
    }
}
