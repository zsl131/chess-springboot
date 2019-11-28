package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.VideoRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2019/2/8.
 */
public interface IVideoRecordDao extends BaseRepository<VideoRecord, Integer>, JpaSpecificationExecutor<VideoRecord> {

    @Query("FROM VideoRecord vr WHERE vr.username=?1 AND vr.courseId=?2 AND vr.createLong>=?3")
    List<VideoRecord> findByUserAndTime(String username, Integer courseId, Long time);

    /** 获取用户查看视频的次数 */
    @Query("SELECT COUNT(id) FROM VideoRecord vr WHERE vr.username=?1 AND vr.courseId=?2")
    Integer findCount(String username, Integer courseId);
}
