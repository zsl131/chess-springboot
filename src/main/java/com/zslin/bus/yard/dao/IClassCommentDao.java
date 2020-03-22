package com.zslin.bus.yard.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.yard.model.ClassComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IClassCommentDao extends BaseRepository<ClassComment, Integer>, JpaSpecificationExecutor<ClassComment> {

}
