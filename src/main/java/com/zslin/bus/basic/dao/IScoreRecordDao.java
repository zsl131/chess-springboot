package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.ScoreRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/9/3.
 */
public interface IScoreRecordDao extends BaseRepository<ScoreRecord, Integer>, JpaSpecificationExecutor<ScoreRecord> {

    @Query("SELECT COUNT(id) FROM ScoreRecord s WHERE s.openid=?1 AND s.reasonSn = ?2")
    Integer findTotalAmountByOpenid(String openid, String reasonSn);

    @Query("SELECT COUNT(id) FROM ScoreRecord s WHERE s.openid=?1 AND s.reasonSn=?2 AND s.createDay=?3")
    Integer findDayAmountByOpenid(String openid, String reasonSn, String day);
}
