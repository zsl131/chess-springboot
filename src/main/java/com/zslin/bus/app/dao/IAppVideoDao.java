package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppVideo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IAppVideoDao extends BaseRepository<AppVideo, Integer>, JpaSpecificationExecutor<AppVideo> {

}
