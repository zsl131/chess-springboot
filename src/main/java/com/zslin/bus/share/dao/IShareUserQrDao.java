package com.zslin.bus.share.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.share.model.ShareUserQr;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IShareUserQrDao extends BaseRepository<ShareUserQr, Integer>, JpaSpecificationExecutor<ShareUserQr> {

    ShareUserQr findByRecordIdAndUserId(Integer recordId, Integer userId);

    List<ShareUserQr> findByRecordId(Integer recordId);
}
