package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;

import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
public class SystemTreeDto {

    private ClassSystem system;

    private List<ClassSystemDetail> data;

    public SystemTreeDto() {
    }

    public SystemTreeDto(ClassSystem system, List<ClassSystemDetail> data) {
        this.system = system;
        this.data = data;
    }

    public ClassSystem getSystem() {
        return system;
    }

    public void setSystem(ClassSystem system) {
        this.system = system;
    }

    public List<ClassSystemDetail> getData() {
        return data;
    }

    public void setData(List<ClassSystemDetail> data) {
        this.data = data;
    }
}
