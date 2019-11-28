package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2019/9/7.
 */
@Entity
@Table(name = "y_class_course_tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassCourseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** Tag Id */
    private Integer tid;

    /** Course Id */
    private Integer cid;

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

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
}
