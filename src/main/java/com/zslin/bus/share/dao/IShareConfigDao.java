package com.zslin.bus.share.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.share.model.ShareConfig;
import org.springframework.data.jpa.repository.Query;

public interface IShareConfigDao extends BaseRepository<ShareConfig, Integer> {

    @Query("FROM ShareConfig")
    ShareConfig loadOne();
}
