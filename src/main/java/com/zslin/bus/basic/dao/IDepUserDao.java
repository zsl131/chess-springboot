package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.DepUser;
import com.zslin.bus.basic.model.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/7/19.
 */
public interface IDepUserDao extends BaseRepository<DepUser, Integer> {

    @Query("SELECT du.userId FROM DepUser du WHERE du.depId=?1")
    List<Integer> findAuthUserIds(Integer depId);

    DepUser findByDepIdAndUserId(Integer depId, Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE DepUser WHERE depId=?1")
    void deleteByDepId(Integer depId);

    @Query("SELECT d FROM Department d, DepUser du WHERE d.id=du.depId AND du.userId=?1")
    List<Department> findDepartmentByUserId(Integer userId);

    @Query("SELECT depId FROM DepUser WHERE userId=?1")
    List<Integer> findDepIds(Integer userId);
}
