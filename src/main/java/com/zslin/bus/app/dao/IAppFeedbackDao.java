package com.zslin.bus.app.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.app.model.AppFeedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/9/24.
 */
public interface IAppFeedbackDao extends BaseRepository<AppFeedback, Integer>, JpaSpecificationExecutor<AppFeedback> {
}
