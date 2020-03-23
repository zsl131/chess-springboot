package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCommentDao;
import com.zslin.bus.yard.model.ClassComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AppClassCommentService {

    @Autowired
    private IClassCommentDao classCommentDao;

    public JsonResult listByTea(String params) {
        Integer page = 0;
        try { page = Integer.parseInt(JsonTools.getJsonParam(params, "page")); } catch (Exception e) { } //页码

        String phone = JsonTools.getJsonParam(params, "phone");

        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        ssb.add(new SpecificationOperator("teaPhone", "eq", phone));
        Page<ClassComment> list = classCommentDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));

        return JsonResult.success().set("commentList", list.getContent());
    }
}
