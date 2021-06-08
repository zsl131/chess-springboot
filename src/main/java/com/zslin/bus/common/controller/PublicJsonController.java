package com.zslin.bus.common.controller;

import com.zslin.bus.basic.dao.IAgeDicDao;
import com.zslin.bus.basic.dao.ISchoolDicDao;
import com.zslin.bus.basic.model.SchoolDic;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zsl on 2019/6/27.
 */
@RequestMapping(value = "public/json")
@RestController
public class PublicJsonController {

    @Autowired
    private IAgeDicDao ageDicDao;

    @Autowired
    private ISchoolDicDao schoolDicDao;

    @GetMapping(value = "onAddStudent")
    public JsonResult onAddStudent() {
        JsonResult result = JsonResult.getInstance();
        //List<AgeDic> ageList = ageDicDao.findAll();
        List<SchoolDic> schoolList = schoolDicDao.findAll();
//        result.set("ageList", ageList).set("schoolList", schoolList);
        result.set("schoolList", schoolList);
        return result;
    }
}
