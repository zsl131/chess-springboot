package com.zslin.bus.yard.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassImageDao;
import com.zslin.bus.yard.model.ClassImage;
import com.zslin.bus.yard.tools.TeachPlanConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AdminAuth(name = "课堂影像管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/classImage")
public class ClassImageService {

    @Autowired
    private IClassImageDao classImageDao;

    @Autowired
    private TeachPlanConfigTools teachPlanConfigTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ClassImage> res = classImageDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult listByTeacher(String params) {
        String phone = JsonTools.getJsonParam(params, "phone");
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ClassImage> res = classImageDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("teaPhone", "eq", phone)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult reply(String params) {
        try {
            Integer id = JsonTools.getId(params);
            String reply = JsonTools.getJsonParam(params, "reply");

            ClassImage ci = classImageDao.findOne(id);
            ci.setReply(reply);
            ci.setReplyDate(NormalTools.curDate("yyyy-MM-dd"));
            ci.setReplyTime(NormalTools.curDatetime());
            ci.setReplyLong(System.currentTimeMillis());

            classImageDao.save(ci);

            return JsonResult.success("点评成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult queryByTea(String params) {
        System.out.println(params);
        Integer courseId = JsonTools.getIntegerParams(params, "courseId");
        Integer teaId = JsonTools.getIntegerParams(params, "teaId");
        Integer roomId = JsonTools.getIntegerParams(params, "roomId");
        String year = teachPlanConfigTools.getCurYear();

        List<ClassImage> imageList = classImageDao.findByTea(teaId, year, courseId, roomId);
        return JsonResult.success().set("imageList", imageList);
    }
}
