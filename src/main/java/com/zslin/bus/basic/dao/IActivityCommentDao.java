package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/7/24.
 */
public interface IActivityCommentDao extends BaseRepository<ActivityComment, Integer>, JpaSpecificationExecutor<ActivityComment> {

    @Query("UPDATE ActivityComment a SET a.actTitle=?2 WHERE a.actId=?1")
    @Modifying
    @Transactional
    void updateTitle(Integer id, String title);

    @Query("SELECT COUNT(a.id) FROM ActivityComment a WHERE a.actId=?1")
    Integer queryCountByActivity(Integer actId);

    @Query("UPDATE ActivityComment a SET a.goodCount=a.goodCount+?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateGoodCount(Integer id, Integer count);

    @Query("UPDATE ActivityComment a SET a.status=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);

    @Query("FROM ActivityComment a WHERE a.actId=?1 AND a.status='1'")
    List<ActivityComment> listByActivityId(Integer activityId);
}
