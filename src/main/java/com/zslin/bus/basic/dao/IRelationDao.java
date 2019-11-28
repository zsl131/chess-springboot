package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.Relation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/10/9.
 */
public interface IRelationDao extends BaseRepository<Relation, Integer>, JpaSpecificationExecutor<Relation> {

    Relation findByPhone(String phone);

    Relation findByOpenid(String openid);

    Relation findByUsername(String username);
}
