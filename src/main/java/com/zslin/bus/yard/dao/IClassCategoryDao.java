package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
public interface IClassCategoryDao extends BaseRepository<ClassCategory, Integer>, JpaSpecificationExecutor<ClassCategory> {

    @Query("FROM ClassCategory m WHERE m.status='1' AND (m.pid IS NULL OR m.pid=0)")
    List<ClassCategory> findRootShow(Sort sort);

    @Query("FROM ClassCategory m WHERE (m.pid IS NULL OR m.pid=0)")
    List<ClassCategory> findRoot(Sort sort);

    @Query("FROM ClassCategory m WHERE m.status='1' AND m.pid=?1")
    List<ClassCategory> findByParent(Integer pid, Sort sort);

    /** 获取子元素ID */
    @Query("SELECT c.id FROM ClassCategory c WHERE c.pid=?1")
    List<Integer> findChildrenIds(Integer pid);
}
