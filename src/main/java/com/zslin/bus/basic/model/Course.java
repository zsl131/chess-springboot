package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 课程，没有使用
 */
@Entity
@Table(name = "t_course")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;

    /**课程类别*/
    private String category;

    /**选项*/
    private String options;

    /**选项abcd*/
    private String a;

    private String b;

    private String c;

    private String d;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
