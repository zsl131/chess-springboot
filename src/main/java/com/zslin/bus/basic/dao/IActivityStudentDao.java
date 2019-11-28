package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityStudent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/8/7.
 */
public interface IActivityStudentDao extends BaseRepository<ActivityStudent, Integer>, JpaSpecificationExecutor<ActivityStudent> {

    /**
     * 获取某次活动的报名状态的记录数
     * @param recordId 活动记录Id
     * @param status 要查询的状态
     * @return
     */
    @Query("SELECT COUNT(id) FROM ActivityStudent WHERE recordId=?1 AND status in ?2")
    Integer queryCount(Integer recordId, String ...status);

    @Query("SELECT COUNT(id) FROM ActivityStudent WHERE recordId=?1")
    Integer applyCount(Integer recordId);

    List<ActivityStudent> findByRecordId(Integer recordId);

    List<ActivityStudent> findByRecordIdAndOpenid(Integer recordId, String openid);

    List<ActivityStudent> findByRecordIdAndPhone(Integer recordId, String phone);

    List<ActivityStudent> findByStuId(Integer stuId);

    ActivityStudent findByStuIdAndRecordId(Integer stuId, Integer recordId);

    @Query("UPDATE ActivityStudent a SET a.status=?2, a.rejectReason=?3 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status, String reason);

    @Query("UPDATE ActivityStudent a SET a.hasCheck=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void udpateHasCheck(Integer id, String hasCheck);

    /** 所有报名人数 */
    @Query("SELECT COUNT(id) FROM ActivityStudent")
    Integer countAll();

    /** 报名通过的人数 */
    @Query("SELECT COUNT(id) FROM ActivityStudent WHERE status='1'")
    Integer countPassed();
}
