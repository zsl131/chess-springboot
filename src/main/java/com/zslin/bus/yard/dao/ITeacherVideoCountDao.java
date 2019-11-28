package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.TeacherVideoCount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/2/11.
 */
public interface ITeacherVideoCountDao extends BaseRepository<TeacherVideoCount, Integer>, JpaSpecificationExecutor<TeacherVideoCount> {

    TeacherVideoCount findByUsernameAndCourseId(String username, Integer courseId);
}
