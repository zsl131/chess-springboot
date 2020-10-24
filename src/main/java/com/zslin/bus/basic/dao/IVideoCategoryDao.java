package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.VideoCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IVideoCategoryDao extends BaseRepository<VideoCategory, Integer>, JpaSpecificationExecutor<VideoCategory> {

    @Query("FROM VideoCategory v WHERE v.status='1' ")
    List<VideoCategory> findByShow(Sort sort);
}
