package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "t_activity_record_image")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityRecordImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 图片路径 */
    @Column(name = "img_url")
    private String imgUrl;

    private String title;

    @Column(name = "act_id")
    private Integer actId;

    @Column(name = "act_title")
    private String actTitle;

    @Column(name = "record_id")
    private Integer recordId;

    /** 活动开展时间 */
    @Column(name = "record_hold_time")
    private String recordHoldTime;

    /** 活动开展时间Long */
    @Column(name = "record_hold_time_long")
    private Long recordHoldTimeLong;

    /** 活动开展地点 */
    @Column(name = "record_address")
    private String recordAddress;

    public Long getRecordHoldTimeLong() {
        return recordHoldTimeLong;
    }

    public void setRecordHoldTimeLong(Long recordHoldTimeLong) {
        this.recordHoldTimeLong = recordHoldTimeLong;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getRecordHoldTime() {
        return recordHoldTime;
    }

    public void setRecordHoldTime(String recordHoldTime) {
        this.recordHoldTime = recordHoldTime;
    }

    public String getRecordAddress() {
        return recordAddress;
    }

    public void setRecordAddress(String recordAddress) {
        this.recordAddress = recordAddress;
    }
}
