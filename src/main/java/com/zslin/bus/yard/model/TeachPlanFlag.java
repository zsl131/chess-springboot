package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 教案提交状态
 */
@Entity
@Table(name = "y_teach_plan_flag")
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class TeachPlanFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "sch_id")
    private Integer schId;

    @Column(name = "sch_name")
    private String schName;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "tea_name")
    private String teaName;

    @Column(name = "tea_phone")
    private String teaPhone;

    /** 课程ID */
    @Column(name = "course_id")
    private Integer courseId;

    /** 课程标题 */
    @Column(name = "course_title")
    private String courseTitle;

    /** 教案年份 */
    @Column(name = "plan_year")
    private String planYear;

    /** 状态，0-未提交；1-已提交 */
    private String flag;
}
