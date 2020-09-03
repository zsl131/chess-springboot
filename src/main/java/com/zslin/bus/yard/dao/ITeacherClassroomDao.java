package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.TeacherClassroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITeacherClassroomDao extends BaseRepository<TeacherClassroom, Integer>, JpaSpecificationExecutor<TeacherClassroom> {

    /** 获取教师对应年份的班级信息 */
    List<TeacherClassroom> findByTargetYearAndTeaId(String targetYear, Integer teaId);

    Page<TeacherClassroom> findByTeaId(Integer teaId, Pageable pageable);

    /** 获取其中一条 */
    @Query("FROM TeacherClassroom t WHERE t.sid=?1 AND t.targetYear=?2 AND t.teaId=?3")
    List<TeacherClassroom> queryBySystemId(Integer sid, String targetYear, Integer teaId);

    @Query("SELECT t FROM TeacherClassroom t, SystemCourse c WHERE c.sid=t.sid AND t.targetYear=?2 AND t.teaId=?3 AND c.cid=?1 GROUP BY t.sid")
    List<TeacherClassroom> queryByCourseId(Integer cid, String targetYear, Integer teaId);

//    List<TeacherClassroom> queyrB
}
