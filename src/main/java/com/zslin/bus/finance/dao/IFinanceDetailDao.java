package com.zslin.bus.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.finance.dto.FinanceCountDto;
import com.zslin.bus.finance.model.FinanceDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2019/1/2.
 */
public interface IFinanceDetailDao extends BaseRepository<FinanceDetail, Integer>, JpaSpecificationExecutor<FinanceDetail> {

    @Query("SELECT SUM(amount) FROM FinanceDetail WHERE status='1' AND flag=?1")
    Float sum(String flag);

    @Query("UPDATE FinanceDetail SET status=?1, invalidReason=?2, invalidName=?3, invalidPhone=?4 WHERE id=?5")
    @Modifying
    @Transactional
    void updateStatus(String status, String reason, String name, String phone, Integer id);

    @Query("UPDATE FinanceDetail SET status=?1, invalidReason=?2, invalidName=?3, invalidPhone=?4 WHERE ticketNo=?5")
    @Modifying
    @Transactional
    void updateStatus(String status, String reason, String name, String phone, String ticketNo);

    @Query("SELECT MAX(tno) FROM FinanceDetail WHERE recordMonth=?1")
    Integer maxTicketNo(String month);

    @Query("SELECT new com.zslin.bus.finance.dto.FinanceCountDto(recordMonth, SUM(amount)) FROM FinanceDetail WHERE status='1' AND recordYear=?1 AND flag=?2 GROUP BY recordMonth ORDER BY recordMonth DESC")
    List<FinanceCountDto> findCountGroupByMonth(String year, String flag);

    @Query("SELECT new com.zslin.bus.finance.dto.FinanceCountDto(recordMonth, SUM(amount)) FROM FinanceDetail WHERE status='1' AND flag=?1 GROUP BY recordMonth ORDER BY recordMonth DESC")
    List<FinanceCountDto> findCountAllGroupByMonth(String flag);

    @Query("SELECT new com.zslin.bus.finance.dto.FinanceCountDto(cateName, SUM(amount)) FROM FinanceDetail WHERE status='1' AND recordYear=?1 AND flag=?2 GROUP BY cateId ORDER BY recordMonth DESC")
    List<FinanceCountDto> findCountGroupByCate(String year, String flag);

    @Query("SELECT new com.zslin.bus.finance.dto.FinanceCountDto(cateName, SUM(amount)) FROM FinanceDetail WHERE status='1' AND flag=?1 GROUP BY cateId ORDER BY recordMonth DESC")
    List<FinanceCountDto> findCountAllGroupByCate(String flag);

    List<FinanceDetail> findByTicketNo(String ticketNo);
}
