package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.AgeDic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/7/23.
 */
public interface IAgeDicDao extends BaseRepository<AgeDic, Integer>, JpaSpecificationExecutor<AgeDic> {

    @Query("SELECT name from AgeDic WHERE id=?1")
    String findNameById(Integer id);
}
