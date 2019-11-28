package com.zslin.bus.dto;

/**
 * 导出学员统计信息DTO对象
 */
public class StudentCountDto {
    private String name;
    private String phone;
    private Integer total;
    //通过次数
    private Integer passCount;
    //签到次数
    private Integer signCount;
    //签到率
    private Double signPercent;

    @Override
    public String toString() {
        return "CountDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", total=" + total +
                ", passCount=" + passCount +
                ", signCount=" + signCount +
                ", signPercent=" + signPercent +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public Double getSignPercent() {
        return signPercent;
    }

    public void setSignPercent(Double signPercent) {
        this.signPercent = signPercent;
    }
}
