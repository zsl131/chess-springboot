package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 其他登陆，用于微信登陆或短信登陆
 * Created by zsl on 2018/10/17.
 */
@Entity
@Table(name = "t_other_login")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 用户名 */
    private String username;

    /** 随机生成的认证Token */
    private String token;

    /** 创建时间Long */
    @Column(name = "create_long")
    private Long createLong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }
}
