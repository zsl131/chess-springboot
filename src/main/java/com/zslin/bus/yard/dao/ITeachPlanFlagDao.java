package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.TeachPlanFlag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITeachPlanFlagDao extends BaseRepository<TeachPlanFlag, Integer>, JpaSpecificationExecutor<TeachPlanFlag> {

    @Query("SELECT t.flag FROM TeachPlanFlag t WHERE t.teaId=?1 AND t.courseId=?2 AND t.planYear=?3 ")
    String queryFlag(Integer teaId, Integer courseId, String year);

    @Query("FROM TeachPlanFlag t WHERE t.teaId=?1 AND t.courseId=?2 AND t.planYear=?3 ")
    TeachPlanFlag queryOne(Integer teaId, Integer courseId, String year);

    @Query("FROM TeachPlanFlag t WHERE t.teaId=?1 AND t.planYear=?2")
    List<TeachPlanFlag> findByTea(Integer teaId, String year);
}
