package com.zslin.bus.qrcode.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.qrcode.model.Qrconfig;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2019/3/1.
 */
public interface IQrconfigDao extends BaseRepository<Qrconfig, Integer> {

    @Query("FROM Qrconfig")
    Qrconfig loadOne();
}
