package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 选课管理系统*/
@Entity
@Table(name="t_select_lesson")
@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class SelectLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String lesson;

    @Column(name="create_date")
    private String createDate;

    @Column(name="create_time")
    private String createTime;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
