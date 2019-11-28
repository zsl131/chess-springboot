package com.zslin.bus.basic.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.basic.model.NoticeComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl on 2018/9/22.
 */
public interface INoticeCommentDao extends BaseRepository<NoticeComment, Integer>, JpaSpecificationExecutor<NoticeComment> {

    @Query("SELECT COUNT(id) FROM NoticeComment n WHERE n.noticeId=?1")
    Integer findCount(Integer noticeId);

    @Query("UPDATE NoticeComment a SET a.goodCount=a.goodCount+?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateGoodCount(Integer id, Integer count);

    @Query("UPDATE NoticeComment a SET a.status=?2 WHERE a.id=?1")
    @Modifying
    @Transactional
    void updateStatus(Integer id, String status);

}
