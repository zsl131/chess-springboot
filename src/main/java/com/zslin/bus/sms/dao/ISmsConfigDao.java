package com.zslin.bus.sms.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.sms.model.SmsConfig;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/9/25.
 */
public interface ISmsConfigDao extends BaseRepository<SmsConfig, Integer> {

    @Query("FROM SmsConfig")
    SmsConfig loadOne();
}
