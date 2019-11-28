package com.zslin.bus.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * App滚动图片
 * Created by zsl on 2019/9/22.
 */
@Entity
@Table(name = "a_app_swiper")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppSwiper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 名称 */
    private String name;

    /** 链接地址 */
    private String url;

    /** 状态，0-隐藏；1-显示 */
    private String status;

    /** 序号，越小越靠前 */
    @Column(name = "order_no")
    private Integer orderNo;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
