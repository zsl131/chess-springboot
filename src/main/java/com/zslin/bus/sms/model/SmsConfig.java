package com.zslin.bus.sms.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/9/25.
 */
@Entity
@Table(name = "sms_config")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 发送短信的URL地址 */
    private String url;

    /** 身份认证信息 */
    private String token;

    /** 添加模板的Code */
    @Column(name = "add_module")
    private String addModule;

    /** 删除模板的Code */
    @Column(name = "del_module")
    private String delModule;

    /** 列表模板的Code */
    @Column(name = "list_module")
    private String listModule;

    /** 查询短信余额的Code */
    private String surplus;

    /** 发送短信的Code */
    @Column(name = "send_msg")
    private String sendMsg;

    /** 发送短信的接口ID */
    @Column(name = "send_code_iid")
    private Integer sendCodeIId;

    public Integer getId() {
        return id;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddModule() {
        return addModule;
    }

    public void setAddModule(String addModule) {
        this.addModule = addModule;
    }

    public String getDelModule() {
        return delModule;
    }

    public void setDelModule(String delModule) {
        this.delModule = delModule;
    }

    public String getListModule() {
        return listModule;
    }

    public void setListModule(String listModule) {
        this.listModule = listModule;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public Integer getSendCodeIId() {
        return sendCodeIId;
    }

    public void setSendCodeIId(Integer sendCodeIId) {
        this.sendCodeIId = sendCodeIId;
    }
}
