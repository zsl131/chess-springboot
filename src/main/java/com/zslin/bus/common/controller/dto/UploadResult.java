package com.zslin.bus.common.controller.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2019/9/24.
 */
public class UploadResult {

    private Integer errno = 0;
    private List<String> data;

    public UploadResult(){
        data = new ArrayList<>();
    }

    public UploadResult(Integer errno) {
        this();
        this.errno = errno;
    }

    public UploadResult(Integer errno, String imagePath) {
        this(errno);
        this.errno = errno;
        this.data.add(imagePath);
    }

    public UploadResult add(String imagePath) {
        this.data.add(imagePath);
        return this;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
