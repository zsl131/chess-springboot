package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassCourse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/9/12.
 */
@Component("classCourseDao")
public interface IClassCourseDao extends BaseRepository<ClassCourse, Integer>, JpaSpecificationExecutor<ClassCourse> {

    @Query("UPDATE ClassCourse c SET c.videoId=0 WHERE c.id=?1")
    @Modifying
    @Transactional
    void cleanVideo(Integer id);

    @Query("UPDATE ClassCourse c SET c.pptId=0 WHERE c.id=?1")
    @Modifying
    @Transactional
    void cleanPPT(Integer id);

    @Query("UPDATE ClassCourse c SET c.learnId=0 WHERE c.id=?1")
    @Modifying
    @Transactional
    void cleanLearn(Integer id);

    List<ClassCourse> findByCid(Integer cid);

    List<ClassCourse> findByGradeId(Integer gradeId);

    @Query("SELECT c FROM ClassCourse c, ClassCourseTag t WHERE t.cid = c.id AND t.tid=?1")
    List<ClassCourse> findByTagId(Integer tagId);

    @Query("UPDATE ClassCourse c SET c.tags=?1 WHERE c.id=?2")
    @Modifying
    @Transactional
    void updateTags(String tags, Integer id);

    @Query("UPDATE ClassCourse c SET c.showTest=?1 WHERE c.id=?2")
    @Modifying
    @Transactional
    void updateShowTest(String showTest, Integer id);

    @Query("FROM ClassCourse c WHERE c.id IN(?1)")
    List<ClassCourse> findByIds(List<Integer> ids);

    /** 获取CategoryID对应的CourseID */
    @Query("SELECT c.id FROM ClassCourse c WHERE c.cid=?1")
    List<Integer> findIdsByCategoryId(Integer cid);
}
