package com.zslin.bus.common.dto;

public class AppUserDto {

    private Integer userId;

    private String name;

    private String phone;

    private String token;

    @Override
    public String toString() {
        return "AppUserDto{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public AppUserDto(Integer userId, String name, String phone, String token) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
