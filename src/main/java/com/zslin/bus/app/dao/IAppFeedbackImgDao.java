package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppFeedbackImg;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2019/9/24.
 */
public interface IAppFeedbackImgDao extends BaseRepository<AppFeedbackImg, Integer>, JpaSpecificationExecutor<AppFeedbackImg> {

    List<AppFeedbackImg> findByRandomId(String randomId);

    @Query("UPDATE AppFeedbackImg a SET a.flag=?1 WHERE a.randomId=?2")
    @Modifying
    @Transactional
    void updateFlag(String flag, String randomId);

    @Query("SELECT f FROM AppFeedbackImg f WHERE f.flag=?1 AND f.createLong<?2")
    List<AppFeedbackImg> findByFlagAndCreateLong(String flag, Long createLong);
}
