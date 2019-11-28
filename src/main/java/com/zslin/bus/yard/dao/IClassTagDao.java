package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassTag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2019/9/7.
 */
public interface IClassTagDao extends BaseRepository<ClassTag, Integer>, JpaSpecificationExecutor<ClassTag> {

    ClassTag findByName(String name);

    @Query("FROM ClassTag c WHERE c.id in (?1)")
    List<ClassTag> findByIds(List<Integer> ids);

    @Query("SELECT c.cid FROM ClassCourseTag c WHERE c.tid=?1")
    List<Integer> findCids(Integer tid);
}
