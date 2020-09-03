package com.zslin.bus.yard.tools;

import com.zslin.bus.yard.dao.IClassImageDao;
import com.zslin.bus.yard.dto.ClassImageDto;
import com.zslin.bus.yard.model.TeacherClassroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassImageTools {

    @Autowired
    private IClassImageDao classImageDao;

    public List<ClassImageDto> build(Integer teaId, String year, List<TeacherClassroom> classroomList) {
        List<ClassImageDto> result = new ArrayList<>();
        for(TeacherClassroom tc : classroomList) {
            ClassImageDto dto = new ClassImageDto();
            dto.setClassroom(tc);
            dto.setClassroomId(tc.getId());
            dto.setImageList(classImageDao.findByTea(teaId, year, tc.getId()));
            result.add(dto);
        }
        return result;
    }
}
