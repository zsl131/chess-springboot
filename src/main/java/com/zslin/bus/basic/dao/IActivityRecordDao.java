package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.wx.dto.RecordImageCountDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/7/31.
 */
public interface IActivityRecordDao extends BaseRepository<ActivityRecord, Integer>, JpaSpecificationExecutor<ActivityRecord> {

    @Query("UPDATE ActivityRecord a SET a.actTitle=?2 WHERE a.actId=?1")
    @Modifying
    @Transactional
    void updateTitle(Integer id, String title);

    @Query("UPDATE ActivityRecord a SET a.applyCount = a.applyCount + ?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateApplyCount(Integer id, Integer count);

    @Query("SELECT COUNT(a.id) FROM ActivityRecord a WHERE a.actId=?1")
    Integer queryCountByActivity(Integer actId);

    List<ActivityRecord> findByActId(Integer actId);

    @Query("UPDATE ActivityRecord a SET a.imgCount=a.imgCount+?1 WHERE a.id=?2 ")
    @Modifying
    @Transactional
    void plusImgCount(Integer count, Integer id);

    /** 获取当前活动 */
    @Query("SELECT MAX(a.actId) FROM ActivityRecord a WHERE a.status='1' ")
    Integer findCurrentActivityId();

    @Query("SELECT new com.zslin.bus.wx.dto.RecordImageCountDto(r.id, r.imgCount) FROM ActivityRecord r WHERE r.id IN (?1)")
    List<RecordImageCountDto> queryCountDto(Integer[] recordIds);
}
