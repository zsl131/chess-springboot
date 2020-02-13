package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ActivityRecordImage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IActivityRecordImageDao extends BaseRepository<ActivityRecordImage, Integer>, JpaSpecificationExecutor<ActivityRecordImage> {

    List<ActivityRecordImage> findByRecordId(Integer recordId, Sort sort);
}
