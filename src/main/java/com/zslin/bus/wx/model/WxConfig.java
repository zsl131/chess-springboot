package com.zslin.bus.wx.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/7/20.
 */
@Entity
@Table(name = "wx_config")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class WxConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String url;

    private String appid;

    private String secret;

    private String aeskey;

    private String token;



    @Column(name = "event_temp")
    private String eventTemp;

    @Override
    public String toString() {
        return "WxConfig{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", appid='" + appid + '\'' +
                ", secret='" + secret + '\'' +
                ", aeskey='" + aeskey + '\'' +
                ", token='" + token + '\'' +
                ", eventTemp='" + eventTemp + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEventTemp() {
        return eventTemp;
    }

    public void setEventTemp(String eventTemp) {
        this.eventTemp = eventTemp;
    }
}
