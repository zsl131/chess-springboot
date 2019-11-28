package com.zslin.bus.qiniu.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.qiniu.model.QiniuConfig;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/12/1.
 */
public interface IQiniuConfigDao extends BaseRepository<QiniuConfig,Integer> {

    @Query("FROM QiniuConfig")
    QiniuConfig loadOne();
}
