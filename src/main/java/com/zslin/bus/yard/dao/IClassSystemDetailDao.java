package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassSystemDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/12/22.
 */
public interface IClassSystemDetailDao extends BaseRepository<ClassSystemDetail, Integer>, JpaSpecificationExecutor<ClassSystemDetail> {

    List<ClassSystemDetail> findBySid(Integer sid, Sort sort);

    @Query("UPDATE ClassSystemDetail c SET c.courseTarget=?1, c.courseTitle=?2 WHERE c.courseId=?3")
    @Modifying
    @Transactional
    void updateCourse(String courseTarget, String courseTitle, Integer courseId);

    /** 获取SystemId对应的Detail元素ID */
    @Query("SELECT c.id FROM ClassSystemDetail c WHERE c.sid=?1")
    List<Integer> findIdsBySid(Integer sid);

    /** 获取CourseId对应的Detail元素ID */
    @Query("SELECT c.id FROM ClassSystemDetail c WHERE c.courseId=?1")
    List<Integer> findIdsByCourseId(Integer cid);
}
