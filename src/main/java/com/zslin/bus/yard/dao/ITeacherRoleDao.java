package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.Grade;
import com.zslin.bus.yard.model.TeacherRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2019/3/1.
 */
public interface ITeacherRoleDao extends BaseRepository<TeacherRole, Integer> {

    @Query("SELECT rid FROM TeacherRole WHERE teaId=?1")
    List<Integer> findRoleId(Integer teaId);

    @Query("SELECT r.rid FROM TeacherRole r, Teacher t WHERE t.id=r.teaId AND t.phone=?1")
    List<Integer> findRoleIdByPhone(String phone);

    @Query("SELECT g FROM Grade g WHERE g.id in (SELECT r.rid FROM TeacherRole r, Teacher t WHERE t.id=r.teaId AND t.phone=?1)")
    List<Grade> findRoleByPhone(String phone);

    @Query("DELETE TeacherRole t WHERE t.teaId=?1 AND t.rid=?2")
    @Transactional
    @Modifying
    void delete(Integer tid, Integer rid);
}
