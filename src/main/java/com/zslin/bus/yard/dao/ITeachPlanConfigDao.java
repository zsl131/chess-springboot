package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.TeachPlanConfig;
import org.springframework.data.jpa.repository.Query;

public interface ITeachPlanConfigDao extends BaseRepository<TeachPlanConfig, Integer> {

    @Query("FROM TeachPlanConfig")
    TeachPlanConfig loadOne();
}
