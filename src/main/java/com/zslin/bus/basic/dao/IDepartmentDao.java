package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.Department;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/7/12.
 */
public interface IDepartmentDao extends BaseRepository<Department, Integer>, JpaSpecificationExecutor<Department> {

    @Query("FROM Department WHERE id in ?1")
    List<Department> findByIds(Integer ...ids);
}
