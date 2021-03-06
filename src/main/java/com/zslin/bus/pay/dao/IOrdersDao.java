package com.zslin.bus.pay.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.pay.model.Orders;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IOrdersDao extends BaseRepository<Orders, Integer>, JpaSpecificationExecutor<Orders> {

    Orders findByOrdersNo(String ordersNo);
}
