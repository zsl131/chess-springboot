package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 视频播放记录
 * Created by zsl on 2019/2/8.
 */
@Entity
@Table(name = "y_video_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "create_long")
    private Long createLong;

    private String username;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "sch_id")
    private Integer schId;

    @Column(name = "sch_name")
    private String schName;

    /** 是否记数 */
    @Column(name = "need_count")
    private String needCount = "0";

    /** 视频总时长，单位秒 */
    @Column(name = "video_total_time")
    private Integer videoTotalTime;

    public Integer getVideoTotalTime() {
        return videoTotalTime;
    }

    public void setVideoTotalTime(Integer videoTotalTime) {
        this.videoTotalTime = videoTotalTime;
    }

    public String getNeedCount() {
        return needCount;
    }

    public void setNeedCount(String needCount) {
        this.needCount = needCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Integer getTeaId() {
        return teaId;
    }

    public void setTeaId(Integer teaId) {
        this.teaId = teaId;
    }

    public Integer getSchId() {
        return schId;
    }

    public void setSchId(Integer schId) {
        this.schId = schId;
    }

    public String getSchName() {
        return schName;
    }

    public void setSchName(String schName) {
        this.schName = schName;
    }
}
