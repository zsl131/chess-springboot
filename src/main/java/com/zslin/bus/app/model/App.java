package com.zslin.bus.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * App 配置
 * Created by zsl on 2019/8/20.
 */
@Table(name = "a_app")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 项目名称 */
    @Column(name = "app_name")
    private String appName;

    /** App 图标 */
    private String icon;

    /** 图片域名根目录 */
    @Column(name = "img_domain")
    private String imgDomain;

    public String getImgDomain() {
        return imgDomain;
    }

    public void setImgDomain(String imgDomain) {
        this.imgDomain = imgDomain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
