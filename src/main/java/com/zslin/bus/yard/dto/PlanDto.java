package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.TeachPlan;
import com.zslin.bus.yard.model.TeacherClassroom;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO对象
 */
@Data
public class PlanDto {

    private TeacherClassroom classroom;

    private Integer classroomId;

    private Integer sid;

    private List<TeachPlan> planList;

    public PlanDto(TeacherClassroom classroom) {
        this.classroom = classroom;
        this.classroomId = classroom.getId();
        this.sid = classroom.getSid();
        this.planList = new ArrayList<>();
    }

    public void add(TeachPlan plan) {
        if(planList==null) {planList = new ArrayList<>();}
        planList.add(plan);
    }
}
