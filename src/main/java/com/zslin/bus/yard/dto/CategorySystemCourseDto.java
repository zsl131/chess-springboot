package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;

import java.util.List;

/**
 * Created by zsl on 2018/12/24.
 */
public class CategorySystemCourseDto {

    private ClassCategory category;

    private List<SystemCourseDto> courseList;

    public CategorySystemCourseDto() {
    }

    public CategorySystemCourseDto(ClassCategory category, List<SystemCourseDto> courseList) {
        this.category = category;
        this.courseList = courseList;
    }

    public ClassCategory getCategory() {
        return category;
    }

    public void setCategory(ClassCategory category) {
        this.category = category;
    }

    public List<SystemCourseDto> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<SystemCourseDto> courseList) {
        this.courseList = courseList;
    }
}
