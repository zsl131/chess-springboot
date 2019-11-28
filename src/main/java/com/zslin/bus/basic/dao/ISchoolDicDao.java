package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.SchoolDic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/7/23.
 */
public interface ISchoolDicDao extends BaseRepository<SchoolDic, Integer>, JpaSpecificationExecutor<SchoolDic> {

    @Query("SELECT name from SchoolDic WHERE id=?1")
    String findNameById(Integer id);
}
