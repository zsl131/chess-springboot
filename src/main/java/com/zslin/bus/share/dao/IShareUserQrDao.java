package com.zslin.bus.share.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.share.model.ShareUserQr;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IShareUserQrDao extends BaseRepository<ShareUserQr, Integer>, JpaSpecificationExecutor<ShareUserQr> {

//    ShareUserQr findByRecordIdAndUserId(Integer recordId, Integer userId);
    @Query("FROM ShareUserQr s WHERE s.qrType=?1 AND s.objId=?2 AND s.userId=?3")
    ShareUserQr findByType(String type, Integer objId, Integer userId);

    @Query("FROM ShareUserQr s WHERE s.qrType=?1 AND s.objId=?2")
    List<ShareUserQr> findByRecordId(String type, Integer objId);
}
