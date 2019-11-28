package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/9/12.
 * 课程
 */
@Entity
@Table(name = "y_class_course")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 标题 */
    private String title;

    /** 年级 */
    private String grade;

    /** 年级ID */
    @Column(name = "grade_id")
    private Integer gradeId;

    /** 学期，上下 */
    private String term;

    /** 适合年龄 */
    private String age;

    /** 课程内容 */
    @Lob
    private String content;

    /** 备注信息 */
    @Lob
    private String remark;

    @Column(name = "ppt_id")
    private Integer pptId;

    @Column(name = "video_id")
    private Integer videoId;

    @Column(name = "learn_id")
    private Integer learnId;

    /** category id */
    private Integer cid;

    /** category name */
    private String cname;

    /** category parent id */
    private Integer cpid;

    /** category parent name */
    private String cpname;

    /** 学习目标 */
    @Column(name = "learn_target")
    @Lob
    private String learnTarget;

    /** 标签 */
    private String tags;

    /** 是否显示给测试用户 */
    @Column(name = "show_test")
    private String showTest = "0";

    /** 图片地址 */
    @Column(name = "img_url")
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShowTest() {
        return showTest;
    }

    public void setShowTest(String showTest) {
        this.showTest = showTest;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLearnTarget() {
        return learnTarget;
    }

    public void setLearnTarget(String learnTarget) {
        this.learnTarget = learnTarget;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getCpid() {
        return cpid;
    }

    public void setCpid(Integer cpid) {
        this.cpid = cpid;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getPptId() {
        return pptId;
    }

    public void setPptId(Integer pptId) {
        this.pptId = pptId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getLearnId() {
        return learnId;
    }

    public void setLearnId(Integer learnId) {
        this.learnId = learnId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
