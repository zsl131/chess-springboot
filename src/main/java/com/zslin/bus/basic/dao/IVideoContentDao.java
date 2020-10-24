package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.VideoContent;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IVideoContentDao extends BaseRepository<VideoContent, Integer>, JpaSpecificationExecutor<VideoContent> {

    @Query("FROM VideoContent v WHERE v.cateId=?1 ")
    List<VideoContent> listByCate(Integer cateId, Sort sort);

    @Query("UPDATE VideoContent v SET v.readCount=v.readCount+?1 WHERE v.id=?2 ")
    @Modifying
    @Transactional
    void plusReadCount(Integer count, Integer id);
}
