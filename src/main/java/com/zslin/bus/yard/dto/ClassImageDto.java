package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassImage;
import com.zslin.bus.yard.model.TeacherClassroom;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 影集DTO
 */
@Data
public class ClassImageDto {

    private Integer classroomId;

    private TeacherClassroom classroom;

    private List<ClassImage> imageList;

    public ClassImageDto() {
        imageList = new ArrayList<>();
    }
}
