package com.zslin.bus.test.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.test.model.News;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/7/3.
 */
public interface INewsDao extends BaseRepository<News, Integer>, JpaSpecificationExecutor<News> {

}
