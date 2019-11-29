package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.bus.app.dao.IAppVideoDao;
import com.zslin.bus.app.model.AppVideo;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AppVideoService {

    @Autowired
    private IAppVideoDao appVideoDao;

    /**
     * @return
     */
    public JsonResult listAll(String params) {
        Integer page = 0;
        try {
            page = Integer.parseInt(JsonTools.getJsonParam(params, "page"));
        } catch (Exception e) {
            page = 0;
        }
        JsonResult result = JsonResult.success();
//        System.out.println("========gid::"+gid+", PHONE::"+phone);
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("status", "eq", "1");
        Page<AppVideo> datas = appVideoDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        result.set("data", datas.getContent());
        return result;
    }

    /**
     * 获取一个对象
     * @param params
     * @return
     */
    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        AppVideo video = appVideoDao.findOne(id);
        return JsonResult.succ(video);
    }
}
