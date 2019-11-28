package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 班级
 * Created by zsl on 2018/12/18.
 */
@Entity
@Table(name = "y_classroom")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @Column(name = "sch_name")
    private String schName;

    @Column(name = "sch_id")
    private Integer schId;

    @Column(name = "tea_name")
    private String teaName;

    @Column(name = "tea_phone")
    private String teaPhone;

    @Column(name = "tea_id")
    private Integer teaId;

    /** 人数 */
    @Column(name = "stu_count")
    private Integer stuCount = 0;


}
