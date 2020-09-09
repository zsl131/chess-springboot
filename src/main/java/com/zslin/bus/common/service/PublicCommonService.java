package com.zslin.bus.common.service;

import com.zslin.bus.basic.dao.IAgeDicDao;
import com.zslin.bus.basic.dao.ISchoolDicDao;
import com.zslin.bus.basic.model.AgeDic;
import com.zslin.bus.basic.model.SchoolDic;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.tools.SortTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/8/11.
 */
@Service
public class PublicCommonService {

    @Autowired
    private IAgeDicDao ageDicDao;

    @Autowired
    private ISchoolDicDao schoolDicDao;

    @Autowired
    private SortTools sortTools;

    public JsonResult listAllAge(String params) {
        List<AgeDic> list = ageDicDao.findAll();
        return JsonResult.succ(list);
    }

    public JsonResult listAllSchool(String params) {
        List<SchoolDic> list = schoolDicDao.findAll();
        return JsonResult.succ(list);
    }

    /** 设置排序 */
    public JsonResult changeOrderNo(String params) {
        String type = JsonTools.getJsonParam(params, "type");
        String data = JsonTools.getJsonParam(params, "data");
        sortTools.handler(type, data);

        return JsonResult.success("设置成功");
    }
}
