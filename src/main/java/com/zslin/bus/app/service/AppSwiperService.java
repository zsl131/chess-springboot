package com.zslin.bus.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.app.dao.IAppSwiperDao;
import com.zslin.bus.app.model.AppSwiper;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by zsl on 2019/9/22.
 */
@Service
@AdminAuth(name = "滚图管理", psn = "移动端管理", url = "/admin/appSwiper", type = "1", orderNum = 4)
public class AppSwiperService {

    @Autowired
    private IAppSwiperDao appSwiperDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<AppSwiper> res = appSwiperDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 获取对象
     * @param params {id:0}
     * @return
     */
    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            AppSwiper o = appSwiperDao.findOne(id);
            return JsonResult.succ(o);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        AppSwiper obj = JSONObject.toJavaObject(JSON.parseObject(params), AppSwiper.class);
        if(obj.getUrl()!=null) {
            obj.setUrl(obj.getUrl().replaceAll("\\\\", "/"));
        }
        if(obj.getId()!=null && obj.getId()>0) { //修改
            AppSwiper s = appSwiperDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s);
            appSwiperDao.save(s);
        } else {
            appSwiperDao.save(obj);
        }
        return JsonResult.succ(obj);
    }

    /**
     * 删除对象
     * @param params {id:0}
     * @return
     */
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            AppSwiper as = appSwiperDao.findOne(id);
            if(as.getUrl()!=null) {
                File f = new File(configTools.getUploadPath() + as.getUrl());
                if(f.exists()) {f.delete();}
            }
            appSwiperDao.delete(as);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 移动端面获取滚图片 */
    public JsonResult listSwiper(String params) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("status", "eq", "1");
        Page<AppSwiper> list = appSwiperDao.findAll(ssb.generate(), SimplePageBuilder.generate(0, SimpleSortBuilder.generateSort("orderNo_a")));
        return JsonResult.success().set("list", list.getContent());
    }
}
