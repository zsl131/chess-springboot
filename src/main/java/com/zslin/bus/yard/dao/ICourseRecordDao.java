package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.CourseRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/9/11.
 */
public interface ICourseRecordDao extends BaseRepository<CourseRecord, Integer>, JpaSpecificationExecutor<CourseRecord> {

    CourseRecord findByCourseIdAndTeaPhone(Integer courseId, String teaPhone);
}
