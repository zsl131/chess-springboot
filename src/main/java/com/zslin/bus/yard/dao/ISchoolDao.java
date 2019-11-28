package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.School;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/9/10.
 */
public interface ISchoolDao extends BaseRepository<School, Integer>, JpaSpecificationExecutor<School> {

    @Query("UPDATE School s SET s.status=?2 WHERE s.id=?1")
    @Modifying
    @Transactional
    void udpateStatus(Integer id, String status);

    /** 通过手机号码获取对应学校的体系ID */
    @Query("SELECT s.systemId FROM School s , Teacher t WHERE s.id=t.schoolId AND t.phone=?1")
    Integer findSystemId(String username);
}
