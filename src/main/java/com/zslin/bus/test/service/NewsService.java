package com.zslin.bus.test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.annotations.ApiCodeClass;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.test.dao.INewsDao;
import com.zslin.bus.test.model.News;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/5.
 */
@Service
@ApiCodeClass
public class NewsService {

    @Autowired
    private INewsDao newsDao;

    public JsonResult listNews(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<News> res = newsDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("datas", res.getContent());
    }

    public JsonResult saveNews(String params) {
//        System.out.println("params::   "+ params);
        try {
            News news = JSONObject.toJavaObject(JSON.parseObject(params), News.class);
            newsDao.save(news);
            return JsonResult.succ(news);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
