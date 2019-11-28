package com.zslin.bus.basic.model;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name="t_contacts")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;


    private String name;

    //性别
    private String sex;

    //单位名称
    @Column(name="dep_name")
    private String depName;

    //职务
    private String duty;


    private String phone;

    //备注
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
