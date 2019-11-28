package com.zslin.bus.qrcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 二维码配置
 * Created by zsl on 2019/3/1.
 */
@Entity
@Table(name = "q_qrconfig")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Qrconfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "base_url")
    private String baseUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
