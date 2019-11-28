package com.zslin.bus.test.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.test.model.Article;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IArticleDao extends BaseRepository<Article,Integer>,JpaSpecificationExecutor<Article> {
}
