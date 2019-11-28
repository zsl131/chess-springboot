package com.zslin.bus.wx.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.wx.model.WxAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/7/20.
 */
public interface IWxAccountDao extends BaseRepository<WxAccount, Integer>, JpaSpecificationExecutor<WxAccount> {

    WxAccount findByOpenid(String openid);

    @Query("UPDATE WxAccount a SET a.status=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);

    @Query("UPDATE WxAccount a SET a.type=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateType(Integer id, String type);

    @Query("UPDATE WxAccount a SET a.type=?2 WHERE a.phone=?1")
    @Modifying
    @Transactional
    void updateTypeByPhone(String phone, String type);

    @Query("UPDATE WxAccount a SET a.phone=?1, a.bindPhone='1' WHERE a.openid=?2")
    @Modifying
    @Transactional
    void modifyPhone(String phone, String openid);

    @Query("UPDATE WxAccount a SET a.status=?2 WHERE a.openid=?1")
    @Modifying
    @Transactional
    void updateStatus(String openid, String status);

    @Query("UPDATE WxAccount a SET a.followUserId=?2, a.followUserName=?3 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateFollow(Integer id, Integer followId, String followName);

    WxAccount findByPhone(String phone);

    @Query("SELECT a.openid FROM WxAccount a WHERE a.type=?1")
    List<String> findOpenid(String type);

    //获取用户所拉进来的用户数量
    @Query("SELECT COUNT(id) FROM WxAccount a WHERE a.followUserId=?1")
    Integer findPullCount(Integer id);

    @Query("SELECT COUNT(id) FROM WxAccount ")
    Integer findAllCount();

    @Query("SELECT COUNT(id) FROM WxAccount a WHERE a.status='1'")
    Integer findFollowCount();

    @Query("SELECT COUNT(id) FROM WxAccount a WHERE a.createDate=?1 AND a.status='1'")
    Integer findFollowCountByDay(String day);

    @Query("SELECT a.openid FROM WxAccount a WHERE a.phone=?1")
    String findOpenidByPhone(String phone);

    @Query("SELECT a.phone FROM WxAccount a WHERE a.openid=?1")
    String findPhoneByOpenid(String openid);

    @Query("UPDATE WxAccount a SET a.type=?2 WHERE a.openid=?1")
    @Modifying
    @Transactional
    void updateType(String openid, String type);

    @Query("UPDATE WxAccount a SET a.score=a.score+?2 WHERE a.openid=?1")
    @Modifying
    @Transactional
    void updateScore(String openid, Integer score);
}
