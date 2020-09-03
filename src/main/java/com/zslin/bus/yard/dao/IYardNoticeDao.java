package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.YardNotice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IYardNoticeDao extends BaseRepository<YardNotice, Integer>, JpaSpecificationExecutor<YardNotice> {

    /** 获取所有显示的公告 */
    @Query("FROM YardNotice y WHERE y.status='1' ")
    List<YardNotice> findShow(Sort sort);
}
