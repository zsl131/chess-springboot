package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.GradeRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/2/25.
 */
public interface IGradeRoleDao extends BaseRepository<GradeRole, Integer>, JpaSpecificationExecutor<GradeRole> {

}
