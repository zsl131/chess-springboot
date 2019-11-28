package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/8/7.
 */
public interface IStudentDao extends BaseRepository<Student, Integer>, JpaSpecificationExecutor<Student> {

    List<Student> findByOpenid(String openid);

    @Query("SELECT s.name FROM Student s WHERE s.id=?1")
    String findStuName(Integer id);

    @Modifying
    @Transactional
    void deleteByOpenidAndId(String openid, Integer id);
}
