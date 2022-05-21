package com.zslin.bus.share.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.share.model.ShareImg;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IShareImgDao extends BaseRepository<ShareImg, Integer>, JpaSpecificationExecutor<ShareImg> {
}
