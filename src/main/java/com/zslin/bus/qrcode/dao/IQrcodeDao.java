package com.zslin.bus.qrcode.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.qrcode.model.Qrcode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/3/1.
 */
public interface IQrcodeDao extends BaseRepository<Qrcode, Integer>, JpaSpecificationExecutor<Qrcode> {
}
