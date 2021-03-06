package com.zslin.bus.pay.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.pay.model.UnifiedOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUnifiedOrderDao extends BaseRepository<UnifiedOrder, Integer>, JpaSpecificationExecutor<UnifiedOrder> {
}
