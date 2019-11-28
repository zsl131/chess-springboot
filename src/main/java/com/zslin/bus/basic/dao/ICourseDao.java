package com.zslin.bus.basic.dao;

import com.zslin.bus.basic.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ICourseDao extends JpaRepository<Course,Integer>,JpaSpecificationExecutor<Course> {
}
