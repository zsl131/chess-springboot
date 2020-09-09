package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 教案管理
 */
@Entity
@Table(name = "y_teach_plan")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeachPlan {

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

    /** 教案对应学期 */
    @Column(name = "plan_term")
    private String planTerm;

    /** 知识点导入 */
    @Lob
    private String guide;

    /** 授课过程 */
    @Column(name = "teach_step")
    @Lob
    private String teachStep;

    /** 课程过渡，即接下来如何授课 */
    @Column(name = "next_teach")
    @Lob
    private String nextTeach;

    /** 授课目标，即情感目标 */
    @Column(name = "teach_target")
    @Lob
    private String teachTarget;

    /** 重点难点 */
    @Column(name = "key_point")
    @Lob
    private String keyPoint;

    /** 与TeacherClassroom 中的GradeId关联 */
    @Column(name = "grade_id")
    private Integer gradeId;

    /** 与TeacherClassroom 中的GradeName关联 */
    @Column(name = "grade_name")
    private String gradeName;

    /** 体系ID */
    private Integer sid;

    /** 体系名称 */
    private String sname;

    /** 知识点名称 */
    @Column(name = "block_name")
    private String blockName;

    /** 序号 */
    @Column(name = "order_no")
    private Integer orderNo;

    @Column(name = "create_day")
    private String createDay;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;

    @Column(name = "update_day")
    private String updateDay;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "update_long")
    private Long updateLong;
}
