package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.Grade;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/12/14.
 */
public interface IGradeDao extends BaseRepository<Grade, Integer>, JpaSpecificationExecutor<Grade> {

    @Query("SELECT g.name FROM Grade g WHERE g.id=?1")
    String findNameById(Integer id);

    /** 获取用于教师选择的年级 */
    @Query("FROM Grade g WHERE g.teacherFlag='1' AND g.sid IS NOT NULL AND g.sid>0 ")
    List<Grade> findByTeacherFlag(Sort sort);
}
