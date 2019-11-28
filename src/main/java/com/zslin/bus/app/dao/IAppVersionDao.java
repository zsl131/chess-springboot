package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppVersion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2019/10/1.
 */
public interface IAppVersionDao extends BaseRepository<AppVersion, Integer>, JpaSpecificationExecutor<AppVersion> {

    @Query("SELECT a FROM AppVersion a WHERE a.id=(SELECT MAX(v.id) FROM AppVersion v WHERE a.flag='1' AND a.appid=?1)")
    AppVersion loadOne(String appid);

    @Query("SELECT a FROM AppVersion a WHERE a.id=(SELECT MAX(v.id) FROM AppVersion v WHERE a.flag='1')")
    AppVersion loadOne();

    @Query("UPDATE AppVersion a SET a.status=?1 WHERE a.id=?2")
    @Modifying
    @Transactional
    void updateStatus(String status, Integer id);

    @Query("UPDATE AppVersion a SET a.flag=?1 WHERE a.id=?2")
    @Modifying
    @Transactional
    void updateFlag(String flag, Integer id);

    @Query("UPDATE AppVersion a SET a.isForce=?1 WHERE a.id=?2")
    @Modifying
    @Transactional
    void updateForce(String isForce, Integer id);
}
