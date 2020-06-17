package com.zslin.bus.basic.dto;

public class ActivityRecordImageDto {

    private Integer amount;

    private String imgUrl;

    private String title;

    private String recordHoldTime;

    private String recordAddress;

    private Integer recordId;

    private Integer actId;

    public ActivityRecordImageDto() {
    }

    public ActivityRecordImageDto(Integer amount, String imgUrl, String title, String recordHoldTime, String recordAddress, Integer recordId, Integer actId) {
        this.amount = amount;
        this.imgUrl = imgUrl;
        this.title = title;
        this.recordHoldTime = recordHoldTime;
        this.recordAddress = recordAddress;
        this.recordId = recordId;
        this.actId = actId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }
}
