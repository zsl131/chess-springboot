package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.App;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2019/8/20.
 */
public interface IAppDao extends BaseRepository<App, Integer>, JpaSpecificationExecutor<App> {

    @Query("FROM App ")
    App loadOne();
}
