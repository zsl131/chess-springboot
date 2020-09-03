package com.zslin.bus.yard.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.yard.dao.ITeachPlanConfigDao;
import com.zslin.bus.yard.model.TeachPlanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 教案配置工具类
 */
@Component
public class TeachPlanConfigTools {

    @Autowired
    private ITeachPlanConfigDao teachPlanConfigDao;

    /**
     * 获取当前管理的年份
     * @return
     */
    public String getCurYear() {
        TeachPlanConfig config = teachPlanConfigDao.loadOne();
        return config==null? NormalTools.curDate("yyyy"):config.getConfigYear();
    }
}
