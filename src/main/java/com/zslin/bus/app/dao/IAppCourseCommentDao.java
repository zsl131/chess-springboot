package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppCourseComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2019/9/18.
 */
public interface IAppCourseCommentDao extends BaseRepository<AppCourseComment, Integer>, JpaSpecificationExecutor<AppCourseComment> {

    @Query("UPDATE AppCourseComment a SET a.goodCount=a.goodCount+1 WHERE a.id=?1")
    @Modifying
    @Transactional
    void onGood(Integer id);
}
