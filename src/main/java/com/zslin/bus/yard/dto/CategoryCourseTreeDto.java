package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;

import java.util.List;

/**
 * Created by zsl on 2018/12/20.
 */
public class CategoryCourseTreeDto {

    private ClassCategory category;

    private List<CourseTreeDto> courseList;

    public CategoryCourseTreeDto() {
    }

    public CategoryCourseTreeDto(ClassCategory category, List<CourseTreeDto> courseList) {
        this.category = category;
        this.courseList = courseList;
    }

    public ClassCategory getCategory() {
        return category;
    }

    public void setCategory(ClassCategory category) {
        this.category = category;
    }

    public List<CourseTreeDto> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseTreeDto> courseList) {
        this.courseList = courseList;
    }
}
