package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.TeacherClassroom;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 未写教案的课程DTO对象
 */
@Data
public class PlanCourseDto {

    private TeacherClassroom classroom;

    private Integer sid;

    private Integer classroomId;

    private List<ClassCourse> courseList;

    private List<Integer> courseIds;

    public PlanCourseDto(TeacherClassroom classroom) {
        this.classroom = classroom;
        this.sid = classroom.getSid();
        this.classroomId = classroom.getId();
        this.courseList = new ArrayList<>();
        this.courseIds = new ArrayList<>();
    }

    public void add(ClassCourse course) {
        if(this.courseList==null) {this.courseList = new ArrayList<>();}
        this.courseList.add(course);
    }

    public void add(Integer courseId) {
        if(this.courseIds==null) {this.courseIds = new ArrayList<>();}
        this.courseIds.add(courseId);
    }
}
