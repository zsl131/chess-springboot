package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassSystem;

import java.util.List;

/**
 * Created by zsl on 2018/12/20.
 */
public class SystemDetailTreeDto {

    private ClassSystem system;

    private List<SystemTreeDto> detailList;

    public SystemDetailTreeDto() {
    }

    public SystemDetailTreeDto(ClassSystem system, List<SystemTreeDto> detailList) {
        this.system = system;
        this.detailList = detailList;
    }

    public ClassSystem getSystem() {
        return system;
    }

    public void setSystem(ClassSystem system) {
        this.system = system;
    }

    public List<SystemTreeDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<SystemTreeDto> detailList) {
        this.detailList = detailList;
    }
}
