package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 课堂影像
 */
@Entity
@Table(name = "y_class_image")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "tea_name")
    private String teaName;

    @Column(name = "tea_phone")
    private String teaPhone;

    @Column(name = "sch_id")
    private Integer schId;

    @Column(name = "sch_name")
    private String schName;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "create_year")
    private String createYear;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;

    @Column(name = "file_type")
    private String fileType;

    private String url;

    /** 状态，1-显示；0-不显示 */
    private String status;

    /** 班级ID */
    @Column(name = "room_id")
    private Integer roomId;

    /** 班级名称 */
    @Column(name = "room_name")
    private String roomName;

    /** 学期 */
    private String term;

    /** 归属年份 */
    @Column(name = "own_year")
    private String ownYear;

    @Lob
    private String reply;

    @Column(name = "reply_date")
    private String replyDate;

    @Column(name = "reply_time")
    private String replyTime;

    @Column(name = "reply_long")
    private Long replyLong;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getOwnYear() {
        return ownYear;
    }

    public void setOwnYear(String ownYear) {
        this.ownYear = ownYear;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public Long getReplyLong() {
        return replyLong;
    }

    public void setReplyLong(Long replyLong) {
        this.replyLong = replyLong;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeaId() {
        return teaId;
    }

    public void setTeaId(Integer teaId) {
        this.teaId = teaId;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getTeaPhone() {
        return teaPhone;
    }

    public void setTeaPhone(String teaPhone) {
        this.teaPhone = teaPhone;
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

    public String getCreateYear() {
        return createYear;
    }

    public void setCreateYear(String createYear) {
        this.createYear = createYear;
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

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
