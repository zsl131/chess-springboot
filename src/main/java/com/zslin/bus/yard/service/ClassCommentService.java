package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.qiniu.tools.QiniuUploadTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCommentDao;
import com.zslin.bus.yard.model.ClassComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AdminAuth(name = "课堂点评管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/classComment")
public class ClassCommentService {

    @Autowired
    private IClassCommentDao classCommentDao;

    @Autowired
    private QiniuUploadTools qiniuUploadTools;

    public JsonResult listAll(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ClassComment> res = classCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult listByTea(String params) {
        Integer teaId = Integer.parseInt(JsonTools.getJsonParam(params, "teaId"));

        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ClassComment> res = classCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                                new SpecificationOperator("teaId", "eq", teaId)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult add(String params) {
        try {
            ClassComment cc = JSONObject.toJavaObject(JSON.parseObject(params), ClassComment.class);
            cc.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
            cc.setCreateTime(NormalTools.curDatetime());
            cc.setCreateLong(System.currentTimeMillis());
            classCommentDao.save(cc);
            return JsonResult.success("保存成功");
        } catch (Exception e) {
//            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult reply(String params) {
        Integer id = JsonTools.getId(params);
        String reply = JsonTools.getJsonParam(params, "reply");
        ClassComment cc = classCommentDao.findOne(id);
        cc.setContent(reply);
        classCommentDao.save(cc);
        return JsonResult.success("点评成功");
    }

    public JsonResult delete(String params) {
        try {
            Integer id = JsonTools.getId(params);
            classCommentDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
