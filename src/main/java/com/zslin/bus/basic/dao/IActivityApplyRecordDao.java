package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityApplyRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/7/31.
 */
public interface IActivityApplyRecordDao extends BaseRepository<ActivityApplyRecord, Integer>, JpaSpecificationExecutor<ActivityApplyRecord> {

    @Query("UPDATE ActivityApplyRecord a SET a.actTitle=?2 WHERE a.actId=?1")
    @Modifying
    @Transactional
    void updateTitle(Integer id, String title);

    @Query("SELECT COUNT(id) FROM ActivityApplyRecord a WHERE a.actRecordId=?1")
    Integer applyCount(Integer recordId);
}
