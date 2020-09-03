package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IYardNoticeDao;
import com.zslin.bus.yard.model.YardNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/12/14.
 */
@Service
@AdminAuth(name = "科普公告管理", psn = "科普进校园", orderNum = 1, type = "1", url = "/yard/yardNotice")
public class YardNoticeService {

    @Autowired
    private IYardNoticeDao yardNoticeDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<YardNotice> res = yardNoticeDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 不分页，用于添加课程
     * @param params
     * @return
     */
    public JsonResult listShow(String params) {
        List<YardNotice> list = yardNoticeDao.findShow(SimpleSortBuilder.generateSort("orderNo_a"));
        return JsonResult.getInstance().set("size", list.size()).set("list", list);
    }

    /**
     * 获取对象
     * @param params {id:0}
     * @return
     */
    public JsonResult loadOne(String params) {
        try {
            Integer id = JsonTools.getId(params);
            YardNotice y = yardNoticeDao.findOne(id);
            return JsonResult.succ(y);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        YardNotice obj = JSONObject.toJavaObject(JSON.parseObject(params), YardNotice.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            YardNotice y = yardNoticeDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, y);
            yardNoticeDao.save(y);
        } else {
            yardNoticeDao.save(obj);
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
            Integer id = JsonTools.getId(params);
            yardNoticeDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
