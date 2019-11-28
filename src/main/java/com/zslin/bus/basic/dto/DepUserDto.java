package com.zslin.bus.basic.dto;

import com.zslin.basic.model.User;

import java.util.List;

/**
 * Created by zsl on 2018/7/19.
 */
public class DepUserDto {

    private List<User> userList;

    private List<Integer> userIds;

    public DepUserDto() {
    }

    public DepUserDto(List<User> userList, List<Integer> userIds) {
        this.userList = userList;
        this.userIds = userIds;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
