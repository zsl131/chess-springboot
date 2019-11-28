package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.Attachment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/9/12.
 */
public interface IAttachmentDao extends BaseRepository<Attachment, Integer>, JpaSpecificationExecutor<Attachment> {
}
