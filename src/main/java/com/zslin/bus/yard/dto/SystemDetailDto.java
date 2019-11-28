package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;

import java.util.List;

/**
 * Created by zsl on 2019/4/10.
 */
public class SystemDetailDto {

    private ClassSystem system;

    private List<ClassSystemDetail> detailList;

    public SystemDetailDto(ClassSystem system, List<ClassSystemDetail> detailList) {
        this.system = system;
        this.detailList = detailList;
    }

    public ClassSystem getSystem() {
        return system;
    }

    public void setSystem(ClassSystem system) {
        this.system = system;
    }

    public List<ClassSystemDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ClassSystemDetail> detailList) {
        this.detailList = detailList;
    }
}
