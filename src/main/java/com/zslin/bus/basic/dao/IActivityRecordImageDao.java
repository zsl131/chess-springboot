package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityRecordImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IActivityRecordImageDao extends BaseRepository<ActivityRecordImage, Integer>, JpaSpecificationExecutor<ActivityRecordImage> {

    List<ActivityRecordImage> findByRecordId(Integer recordId, Sort sort);

    @Query("FROM ActivityRecordImage a GROUP BY a.recordId")
    Page<ActivityRecordImage> find4Page(Pageable pageable);

    @Query("UPDATE ActivityRecordImage a SET a.recordHoldTimeLong=?1, a.recordHoldTime=?2 WHERE a.recordId=?3")
    @Modifying
    @Transactional
    void updateHoldTime(Long holdTimeLong, String holdTime, Integer recordId);
}
