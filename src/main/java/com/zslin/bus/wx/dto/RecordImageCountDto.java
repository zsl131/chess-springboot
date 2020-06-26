package com.zslin.bus.wx.dto;

public class RecordImageCountDto {

    private Integer recordId;

    private Integer amount;

    @Override
    public String toString() {
        return "RecordImageCountDto{" +
                "recordId=" + recordId +
                ", amount=" + amount +
                '}';
    }

    public RecordImageCountDto() {
    }

    public RecordImageCountDto(Integer recordId, Integer amount) {
        this.recordId = recordId;
        this.amount = amount;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
