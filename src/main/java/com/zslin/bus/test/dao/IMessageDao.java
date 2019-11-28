package com.zslin.bus.test.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.test.model.Message;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IMessageDao extends BaseRepository<Message,Integer>,JpaSpecificationExecutor<Message> {
}

