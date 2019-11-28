package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassSystemContent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2018/9/12.
 */
public interface IClassSystemContentDao extends BaseRepository<ClassSystemContent, Integer>, JpaSpecificationExecutor<ClassSystemContent> {
}
