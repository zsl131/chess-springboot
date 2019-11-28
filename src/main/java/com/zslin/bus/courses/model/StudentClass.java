package com.zslin.bus.courses.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/8/31.
 * 学生课程报名
 */
@Entity
@Table(name = "c_student_class")
public class StudentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


}
