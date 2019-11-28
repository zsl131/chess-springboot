package com.zslin.bus.yard.dto;

/**
 * 教师读取视频次数的DTO对象
 * Created by zsl on 2019/2/12.
 */
public class TeacherVideoCountDto {

    private String username;

    private String teaName;

    private Integer teaId;

    /** 当前剩余次数 */
    private Integer surplusCount;

    /** 充值次数 */
    private Integer plusCount;

    private Integer courseId;

    private String courseTitle;

    public TeacherVideoCountDto(String username, String teaName, Integer teaId, Integer surplusCount, Integer plusCount, Integer courseId, String courseTitle) {
        this.username = username;
        this.teaName = teaName;
        this.teaId = teaId;
        this.surplusCount = surplusCount;
        this.plusCount = plusCount;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public TeacherVideoCountDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public Integer getTeaId() {
        return teaId;
    }

    public void setTeaId(Integer teaId) {
        this.teaId = teaId;
    }

    public Integer getSurplusCount() {
        return surplusCount;
    }

    public void setSurplusCount(Integer surplusCount) {
        this.surplusCount = surplusCount;
    }

    public Integer getPlusCount() {
        return plusCount;
    }

    public void setPlusCount(Integer plusCount) {
        this.plusCount = plusCount;
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
