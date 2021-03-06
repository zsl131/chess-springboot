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

    /**
     * 商户号id
     * @remark 微信支付
     */
    private String mchid;

    /**
     * Api密钥
     * @remark 微信支付
     */
    private String apiKey;

    @Column(name = "pay_notify_url")
    private String payNotifyUrl;

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

    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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
