package com.zslin.bus.finance.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.finance.model.FinanceCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl on 2019/1/3.
 */
public interface IFinanceCategoryDao extends BaseRepository<FinanceCategory, Integer>, JpaSpecificationExecutor<FinanceCategory> {

}
