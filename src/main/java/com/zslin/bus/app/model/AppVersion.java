package com.zslin.bus.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * 移动端升级处理
 * Created by zsl on 2019/10/1.
 */
@Entity
@Table(name = "app_version")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AppVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 升级标志，1-升级；0-不升级 */
    private String status="0";

    /** 升级说明 */
    private String note;

    /** 更新包下载地址，wgt文件地址 */
    private String url;

    /** 安卓APK下载地址 */
    @Column(name = "apk_url")
    private String apkUrl;

    /** 苹果安装包下载地址 */
    @Column(name = "iso_url")
    private String isoUrl;

    /** 版本号 */
    private String version;

    private String appid;

    /** 标记是否有用，如果更新出问题需要切换到老版本时，把最新记录的flag设置为0 */
    private String flag = "1";

    /** 是否强制更新 */
    @Column(name = "is_force")
    private String isForce ="0";

    @Column(name = "create_day")
    private String createDay;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getIsoUrl() {
        return isoUrl;
    }

    public void setIsoUrl(String isoUrl) {
        this.isoUrl = isoUrl;
    }
}
