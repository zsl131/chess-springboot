package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.TeachPlan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITeachPlanDao extends BaseRepository<TeachPlan, Integer>, JpaSpecificationExecutor<TeachPlan> {

    /**
     * 获取教师教案
     * @param teaId 教师ID
     * @param year 方案年份
//     * @param sid 体系id
     * @return
     */
    /*@Query("FROM TeachPlan t WHERE t.teaId=?1 AND t.planYear=?2 AND t.sid=?3")
    List<TeachPlan> findByTeacher1(Integer teaId, String year, Integer sid);*/

    @Query("FROM TeachPlan t WHERE t.teaId=?1 AND t.planYear=?2")
    List<TeachPlan> findByTeacher(Integer teaId, String year, Sort sort);

    @Query("FROM TeachPlan t WHERE t.teaId=?1 AND t.courseId=?2 AND t.planYear=?3 ")
    List<TeachPlan> findByTeacher(Integer teaId, Integer courseId, String year, Sort sort);

    @Query("SELECT MAX(t.orderNo) FROM TeachPlan t WHERE t.teaId=?1 AND t.courseId=?2 AND t.planYear=?3")
    Integer queryMaxOrderNo(Integer teaId, Integer courseId, String year);

    @Query("FROM TeachPlan t WHERE t.teaId=?1 AND t.planYear=?2 GROUP BY t.courseId")
    List<TeachPlan> findByTea(Integer teaId, String year);

    /**
     * 获取教师对应课程的教案
     * @param teaId 教师ID
     * @param year 年份
     * @param courseId 课程
     * @return
     */
    /*@Query("FROM TeachPlan t WHERE t.teaId=?1 AND t.planYear=?2 AND t.courseId=?3")
    TeachPlan findOne(Integer teaId, String year, Integer courseId);*/
}
