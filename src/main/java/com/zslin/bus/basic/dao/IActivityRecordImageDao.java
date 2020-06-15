package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityRecordImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IActivityRecordImageDao extends BaseRepository<ActivityRecordImage, Integer>, JpaSpecificationExecutor<ActivityRecordImage> {

    List<ActivityRecordImage> findByRecordId(Integer recordId, Sort sort);

    @Query("FROM ActivityRecordImage a GROUP BY a.actId")
    Page<ActivityRecordImage> find4Page(Pageable pageable);
}
