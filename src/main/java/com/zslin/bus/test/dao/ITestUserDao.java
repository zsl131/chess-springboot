package com.zslin.bus.test.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.test.model.TestUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/7/5.
 */
public interface ITestUserDao extends BaseRepository<TestUser, Integer>, JpaSpecificationExecutor<TestUser> {
}
