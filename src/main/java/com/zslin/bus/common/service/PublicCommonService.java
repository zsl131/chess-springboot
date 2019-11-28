package com.zslin.bus.common.service;

import com.zslin.bus.basic.dao.IAgeDicDao;
import com.zslin.bus.basic.dao.ISchoolDicDao;
import com.zslin.bus.basic.model.AgeDic;
import com.zslin.bus.basic.model.SchoolDic;
import com.zslin.bus.tools.JsonResult;
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

    public JsonResult listAllAge(String params) {
        List<AgeDic> list = ageDicDao.findAll();
        return JsonResult.succ(list);
    }

    public JsonResult listAllSchool(String params) {
        List<SchoolDic> list = schoolDicDao.findAll();
        return JsonResult.succ(list);
    }
}
