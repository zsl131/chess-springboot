package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassCourse;

import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
public class CourseTreeDto {

    private ClassCategory category;

    private List<ClassCourse> data;

    public CourseTreeDto() {
    }

    public CourseTreeDto(ClassCategory category, List<ClassCourse> data) {
        this.category = category;
        this.data = data;
    }

    public ClassCategory getCategory() {
        return category;
    }

    public void setCategory(ClassCategory category) {
        this.category = category;
    }

    public List<ClassCourse> getData() {
        return data;
    }

    public void setData(List<ClassCourse> data) {
        this.data = data;
    }
}
