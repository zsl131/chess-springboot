package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/9/10.
 */
public interface ITeacherDao extends BaseRepository<Teacher, Integer>, JpaSpecificationExecutor<Teacher> {

    Teacher findByPhone(String phone);

    Teacher findByOpenid(String openid);

    Teacher findByIdentity(String identity);

    @Query("SELECT t.name FROM Teacher t WHERE t.phone=?1")
    String findNameByPhone(String phone);

    @Query("UPDATE Teacher t SET t.password=?1 WHERE t.id=?2")
    @Modifying
    @Transactional
    void updatePwd(String pwd, Integer id);

    @Query("UPDATE Teacher t SET t.isTest=?1 WHERE t.id=?2")
    @Modifying
    @Transactional
    void updateIsTest(String isTest, Integer id);

    /** 获取手机号码对应的教师是否是测试教师 */
    @Query("SELECT t.isTest FROM Teacher t WHERE t.phone=?1")
    String queryIsTest(String phone);
}
