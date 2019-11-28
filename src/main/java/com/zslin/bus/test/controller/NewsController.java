package com.zslin.bus.test.controller;

import com.zslin.bus.test.dao.INewsDao;
import com.zslin.bus.test.model.News;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zsl on 2018/7/3.
 */
@RestController
@RequestMapping(value = "test/news")
public class NewsController {

    @Autowired
    private INewsDao newsDao;

    @GetMapping(value = "listNews")
    public JsonResult listNews(HttpServletRequest request, HttpServletResponse response) {
        List<News> list = newsDao.findAll();
        return JsonResult.getInstance().set("size", list.size()).set("datas", list).set("test", "测试字段");
    }
}
