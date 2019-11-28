package com.zslin.bus.basic.dao;

import com.zslin.bus.basic.model.ScoreRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IScoreRuleDao extends JpaRepository<ScoreRule,Integer>, JpaSpecificationExecutor<ScoreRule> {

    ScoreRule findBySn(String sn);
}
