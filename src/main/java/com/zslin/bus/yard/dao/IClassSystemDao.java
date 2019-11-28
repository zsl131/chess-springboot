package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassSystem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/9/12.
 */
public interface IClassSystemDao extends BaseRepository<ClassSystem, Integer>, JpaSpecificationExecutor<ClassSystem> {

    @Query("FROM ClassSystem m WHERE (m.pid IS NULL OR m.pid=0)")
    List<ClassSystem> findRoot(Sort sort);

    @Query("FROM ClassSystem m WHERE m.pid=?1")
    List<ClassSystem> findByParent(Integer pid, Sort sort);

    @Query("FROM ClassSystem m WHERE m.pid IS NOT NULL")
    List<ClassSystem> findHasParent(Sort sort);

    @Query("FROM ClassSystem m WHERE m.id in ?1")
    List<ClassSystem> findByIds(List<Integer> ids, Sort sort);

    @Query("SELECT m FROM ClassSystem m WHERE m.id IN (SELECT g.sid FROM GradeRoleSystem g, Teacher t, TeacherRole r WHERE g.rid=r.rid AND t.id=r.teaId AND t.phone=?1) ")
    List<ClassSystem> findByPhone(String phone, Sort sort);

    /** 获取子元素ID */
    @Query("SELECT c.id FROM ClassSystem c WHERE c.pid=?1")
    List<Integer> findChildren(Integer pid);
}
