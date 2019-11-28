package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.SystemCourse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/12/22.
 */
public interface ISystemCourseDao extends BaseRepository<SystemCourse, Integer> {

    SystemCourse findBySidAndCid(Integer sid, Integer cid);

    @Modifying
    @Transactional
    void deleteBySidAndCid(Integer sid, Integer cid);

    @Query("SELECT cid FROM SystemCourse WHERE sid=?1")
    List<Integer> findCourseId(Integer systemId);
}
