package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 年级角色课程体系对应关系，多对多
 * Created by zsl on 2019/2/24.
 */
@Entity
@Table(name = "y_grade_role_system")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradeRoleSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** gradeRole ID */
    private Integer rid;

    private Integer sid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}
