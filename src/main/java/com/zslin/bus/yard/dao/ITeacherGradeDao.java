package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.Grade;
import com.zslin.bus.yard.model.TeacherGrade;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2019/9/10.
 */
public interface ITeacherGradeDao extends BaseRepository<TeacherGrade, Integer>, JpaSpecificationExecutor<TeacherGrade> {

    TeacherGrade findByTidAndGid(Integer tid, Integer gid);

    @Query("SELECT t.gid FROM TeacherGrade t WHERE t.tid=?1")
    List<Integer> findGidsByTid(Integer tid);

    @Query("SELECT g FROM Grade g WHERE g.id in (SELECT o.gid FROM TeacherGrade o, Teacher t WHERE t.id=o.tid AND t.phone=?1)")
    List<Grade> findGradeByPhone(String phone, Sort sort);

    @Modifying
    @Transactional
    void deleteByTidAndGid(Integer tid, Integer gid);
}
