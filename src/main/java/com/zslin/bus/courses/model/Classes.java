package com.zslin.bus.courses.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/8/30.
 * 课程
 */
@Entity
@Table(name = "c_classes")
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 课程名称 */
    private String name;

    @Lob
    private String remark;

    /** 价格 */
    private Float price;

    /** 开课日期 */
    @Column(name = "start_date")
    private String startDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
