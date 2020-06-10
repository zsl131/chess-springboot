package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.Activity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/7/12.
 */
public interface IActivityDao extends BaseRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {

    @Query("UPDATE Activity a SET a.readCount=a.readCount+?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateReadCount(Integer id, Integer count);

    @Query("UPDATE Activity a SET a.goodCount=a.goodCount+?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateGoodCount(Integer id, Integer count);

    @Query("UPDATE Activity a SET a.commentCount=a.commentCount+?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateCommentCount(Integer id, Integer count);

    @Query("UPDATE Activity a SET a.recordCount=a.recordCount+?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateRecordCount(Integer id, Integer count);

    /** 开展活动次数 */
    @Query("SELECT SUM(a.recordCount) FROM Activity a ")
    Integer recordCount();

    @Query("SELECT COUNT(a.id) FROM Activity a WHERE a.recordCount=0")
    Integer recordCountZero();

    /** 获取当前活动 */
    @Query("SELECT MAX(a.id) FROM Activity a WHERE a.status='1' ")
    Integer findCurrentId();

//    List<Activity> listNew();
}
