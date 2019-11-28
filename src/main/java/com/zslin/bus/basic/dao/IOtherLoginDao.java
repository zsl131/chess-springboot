package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.OtherLogin;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/10/17.
 */
public interface IOtherLoginDao extends BaseRepository<OtherLogin, Integer> {

    OtherLogin findByUsernameAndToken(String username, String token);

    @Modifying
    @Transactional
    void deleteByUsername(String username);

    //删除创建时间超过半小时的数据
    @Query("DELETE FROM OtherLogin w WHERE w.createLong<?1")
    @Modifying
    @Transactional
    void deleteByCreateLong(long curLong);
}
