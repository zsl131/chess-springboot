package com.zslin.bus.yard.dto;

import java.util.List;

/**
 * Created by zsl on 2019/2/12.
 */
public class TeacherVideoTreeDto {

    private Integer systemId;

    private String systemName;

    private List<TeacherVideoCountDto> data;

    public TeacherVideoTreeDto() {
    }

    public TeacherVideoTreeDto(Integer systemId, String systemName, List<TeacherVideoCountDto> data) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.data = data;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<TeacherVideoCountDto> getData() {
        return data;
    }

    public void setData(List<TeacherVideoCountDto> data) {
        this.data = data;
    }
}
