package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppRemark;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2019/9/25.
 */
public interface IAppRemarkDao extends BaseRepository<AppRemark, Integer>, JpaSpecificationExecutor<AppRemark> {

    @Query("FROM AppRemark")
    AppRemark loadOne();
}
