package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassCourseAtta;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IClassCourseAttaDao extends BaseRepository<ClassCourseAtta, Integer>, JpaSpecificationExecutor<ClassCourseAtta> {

    List<ClassCourseAtta> findByCourseId(Integer courseId);
}
