package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.GradeRoleSystem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2019/3/1.
 */
public interface IGradeRoleSystemDao extends BaseRepository<GradeRoleSystem, Integer> {

    @Query("SELECT sid FROM GradeRoleSystem WHERE rid=?1")
    List<Integer> findSystemId(Integer rid);

    @Query("SELECT s.sid FROM GradeRoleSystem s, TeacherRole r, Teacher t WHERE r.rid=s.rid AND t.id=r.teaId AND t.phone=?1")
    List<Integer> findSystemIdByPhone(String phone);

    @Query("DELETE GradeRoleSystem WHERE rid=?1 AND sid=?2")
    @Modifying
    @Transactional
    void delete(Integer rid, Integer sid);
}
