package com.zslin.bus.test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.bus.test.dao.IArticleDao;
import com.zslin.bus.test.model.Article;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ArticleService {
    @Autowired
    private IArticleDao articleDao;
    public JsonResult add(String params){
        Article a = JSONObject.toJavaObject(JSON.parseObject(params),Article.class);
        articleDao.save(a);
        return JsonResult.succ(a);
    }
    public JsonResult List(String params){
        System.out.println("articleService.list->params:::"+params);
        List<Article> list = articleDao.findAll();
        return JsonResult.getInstance().set("size", list.size()).set("datas", list);
    }

}
