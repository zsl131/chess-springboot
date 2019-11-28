package com.zslin.bus.basic.dao;

import com.zslin.bus.basic.model.SelectLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISelectLessonDao extends JpaRepository<SelectLesson,Integer>,JpaSpecificationExecutor<SelectLesson > {

}
