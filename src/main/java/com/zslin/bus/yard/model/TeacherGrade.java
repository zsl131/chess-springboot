package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 教师与年级的对应关系
 * Created by zsl on 2019/9/10.
 */
@Entity
@Table(name = "y_teacher_grade")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** Teacher ID */
    private Integer tid;

    /** Grade ID */
    private Integer gid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }
}
