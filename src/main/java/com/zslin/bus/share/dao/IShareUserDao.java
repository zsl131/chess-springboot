package com.zslin.bus.share.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.share.model.ShareUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IShareUserDao extends BaseRepository<ShareUser, Integer>, JpaSpecificationExecutor<ShareUser> {
}
