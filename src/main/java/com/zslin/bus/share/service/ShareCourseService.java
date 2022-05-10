package com.zslin.bus.share.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.share.dao.IShareCourseDao;
import com.zslin.bus.share.model.ShareCourse;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AdminAuth(name = "推广课程管理", psn = "推广管理", url = "/admin/shareCourse", type = "1", orderNum = 1)
public class ShareCourseService {

    @Autowired
    private IShareCourseDao shareCourseDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ShareCourse> res = shareCourseDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent());
    }
}
