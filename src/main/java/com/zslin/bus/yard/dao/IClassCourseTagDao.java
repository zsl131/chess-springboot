package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassCourseTag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2019/9/7.
 */
public interface IClassCourseTagDao extends BaseRepository<ClassCourseTag, Integer>, JpaSpecificationExecutor<ClassCourseTag> {

    ClassCourseTag findByTidAndCid(Integer tid, Integer cid);

    @Query("SELECT o.tid FROM ClassCourseTag o WHERE o.cid=?1")
    List<Integer> findTids(Integer cid);
}
