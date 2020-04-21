package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.Draft;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IDraftDao extends BaseRepository<Draft, Integer>, JpaSpecificationExecutor<Draft> {
}
