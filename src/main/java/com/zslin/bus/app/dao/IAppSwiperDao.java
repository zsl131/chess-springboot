package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppSwiper;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/9/22.
 */
public interface IAppSwiperDao extends BaseRepository<AppSwiper, Integer>, JpaSpecificationExecutor<AppSwiper> {
}
