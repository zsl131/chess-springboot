package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 课程知识点配置管理
 * 开启后才可填写和上传教案
 * 关闭后不可操作教案
 */
@Entity
@Table(name = "y_teach_plan_config")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeachPlanConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 学期；1-春季学期；2-秋季学期 */
    private String term;

    @Column(name = "config_year")
    private String configYear;

    /** 开启标记；1-开启；0-关闭 */
    private String flag;
}
