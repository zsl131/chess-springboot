package com.zslin.bus.wx.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.wx.model.WxLogin;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/9/25.
 */
public interface IWxLoginDao extends BaseRepository<WxLogin, Integer> {

    WxLogin findByToken(String token);

    //删除创建时间超过半小时的数据
    @Query("DELETE FROM WxLogin w WHERE w.createLong<?1")
    @Modifying
    @Transactional
    void deleteByCreateLong(long curLong);

    @Query("UPDATE WxLogin w SET w.openid=?1 WHERE w.token=?2")
    @Modifying
    @Transactional
    void updateOpenid(String openid, String token);

    @Query("SELECT openid FROM WxLogin w WHERE w.token=?1")
    String findOpenidByToken(String token);
}
