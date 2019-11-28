package com.zslin.bus.wx.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.wx.model.Feedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/7/20.
 */
public interface IFeedbackDao extends BaseRepository<Feedback, Integer>, JpaSpecificationExecutor<Feedback> {

    @Query("UPDATE Feedback f SET f.status=?2 WHERE f.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);
}
