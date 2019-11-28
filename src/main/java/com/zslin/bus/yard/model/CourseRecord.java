package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 课程访问记录
 * Created by zsl on 2019/9/11.
 */
@Entity
@Table(name = "y_course_record")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "learn_target")
    @Lob
    private String learnTarget;

    private String grade;

    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "first_date")
    private String firstDate;

    @Column(name = "first_day")
    private String firstDay;

    @Column(name = "first_time")
    private String firstTime;

    @Column(name = "fist_long")
    private Long firstLong;

    @Column(name = "last_date")
    private String lastDate;

    @Column(name = "last_day")
    private String lastDay;

    @Column(name = "last_time")
    private String lastTime;

    @Column(name = "last_long")
    private Long lastLong;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "tea_phone")
    private String teaPhone;

    @Column(name = "tea_name")
    private String teaName;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLearnTarget() {
        return learnTarget;
    }

    public void setLearnTarget(String learnTarget) {
        this.learnTarget = learnTarget;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public Long getFirstLong() {
        return firstLong;
    }

    public void setFirstLong(Long firstLong) {
        this.firstLong = firstLong;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public Long getLastLong() {
        return lastLong;
    }

    public void setLastLong(Long lastLong) {
        this.lastLong = lastLong;
    }

    public Integer getTeaId() {
        return teaId;
    }

    public void setTeaId(Integer teaId) {
        this.teaId = teaId;
    }

    public String getTeaPhone() {
        return teaPhone;
    }

    public void setTeaPhone(String teaPhone) {
        this.teaPhone = teaPhone;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }
}
