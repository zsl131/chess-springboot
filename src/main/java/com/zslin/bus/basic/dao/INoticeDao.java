package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.Notice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl on 2018/9/15.
 */
public interface INoticeDao extends BaseRepository<Notice, Integer>, JpaSpecificationExecutor<Notice> {

    @Query("FROM Notice n WHERE n.needSend='1' AND n.status='1'")
    List<Notice> findNeedSend(Sort sort);

    @Query("UPDATE Notice n SET n.commentCount=n.commentCount+?2 WHERE n.id=?1")
    @Modifying
    @Transactional
    void updateCommentCount(Integer id, Integer count);
}
