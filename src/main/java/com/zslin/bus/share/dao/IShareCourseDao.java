package com.zslin.bus.share.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.share.model.ShareCourse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IShareCourseDao extends BaseRepository<ShareCourse, Integer>, JpaSpecificationExecutor<ShareCourse> {
}
