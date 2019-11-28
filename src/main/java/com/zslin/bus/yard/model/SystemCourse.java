package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 课程体系与课程内容的关联关系
 * Created by zsl on 2018/12/22.
 */
@Entity
@Table(name = "y_system_course")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** System ID */
    private Integer sid;

    /** Course ID */
    private Integer cid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
}
