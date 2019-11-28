package com.zslin.bus.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.finance.model.FinanceTicket;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by zsl on 2019/1/9.
 */
public interface IFinanceTicketDao extends BaseRepository<FinanceTicket, Integer>, JpaSpecificationExecutor<FinanceTicket> {

    List<FinanceTicket> findByTicketNo(String ticketNo);
}
