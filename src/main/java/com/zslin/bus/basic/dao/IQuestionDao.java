package com.zslin.bus.basic.dao;

import com.zslin.bus.basic.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IQuestionDao extends JpaRepository<Question,Integer>,JpaSpecificationExecutor<Question> {

}
