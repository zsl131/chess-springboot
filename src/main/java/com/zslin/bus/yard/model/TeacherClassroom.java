package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "y_teacher_classroom")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherClassroom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "tea_id")
    private Integer teaId;

    @Column(name = "tea_name")
    private String teaName;

    @Column(name = "tea_phone")
    private String teaPhone;

    /** 班级名称 */
    @Column(name = "room_name")
    private String roomName;

    /** 归属年份 */
    @Column(name = "target_year")
    private String targetYear;

    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "grade_name")
    private String gradeName;

    private String term;

    /** 对应体系ID */
    private Integer sid;

    /** 对应体系名称 */
    private String sname;
}
