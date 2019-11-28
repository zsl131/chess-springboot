package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 年级角色，用于设置不同年级可访问的课程
 * Created by zsl on 2019/2/24.
 */
@Entity
@Table(name = "y_grade_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
