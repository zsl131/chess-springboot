package com.zslin.bus.courses.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/8/30.
 * 课程章节
 */
@Entity
@Table(name = "c_class_chapter")
public class ClassChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 课程ID */
    @Column(name = "class_id")
    private Integer classId;

    /** 章节名称 */
    private String name;

    /** 备注 */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
