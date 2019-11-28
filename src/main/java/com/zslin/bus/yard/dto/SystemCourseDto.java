package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCourse;

/**
 * Created by zsl on 2018/12/24.
 */
public class SystemCourseDto {

    private boolean hasUse;

    private ClassCourse course;

    public SystemCourseDto() {
    }

    public SystemCourseDto(boolean hasUse, ClassCourse course) {
        this.hasUse = hasUse;
        this.course = course;
    }

    public boolean isHasUse() {
        return hasUse;
    }

    public void setHasUse(boolean hasUse) {
        this.hasUse = hasUse;
    }

    public ClassCourse getCourse() {
        return course;
    }

    public void setCourse(ClassCourse course) {
        this.course = course;
    }
}
