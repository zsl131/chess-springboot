package com.zslin.bus.app.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppCourseCommentDao;
import com.zslin.bus.app.model.AppCourseComment;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.School;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2019/9/18.
 */
@Service
@AdminAuth(name = "课程评论管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/appCourseComment")
public class AppCourseCommentService {

    @Autowired
    private IAppCourseCommentDao appCourseCommentDao;

    @Autowired
    private ITeacherDao teacherDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<AppCourseComment> list = appCourseCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", list.getTotalElements()).set("data", list.getContent());
    }

    /**
     * 回复评论
     * @param params 必须包含 id reply
     * @return
     */
    public JsonResult reply(String params) {
        try {
            Integer id = JsonTools.getId(params);
            String reply = JsonTools.getJsonParam(params, "reply");
            String status = JsonTools.getJsonParam(params, "status");
            AppCourseComment acc = appCourseCommentDao.findOne(id);
            acc.setReply(reply);
            acc.setReplyLong(System.currentTimeMillis());
            acc.setReplyTime(NormalTools.curDatetime());
            acc.setReplyDay(NormalTools.curDate());
            acc.setStatus(status);
            appCourseCommentDao.save(acc);
            return JsonResult.success("保存成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 获取对象，用于回复
     * @param params 必须包含id
     * @return
     */
    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        AppCourseComment acc = appCourseCommentDao.findOne(id);
        return JsonResult.succ(acc);
    }

    /**
     * 点赞
     * @param params
     * @return
     */
    public JsonResult onGood(String params) {
        Integer id = JsonTools.getId(params);
        appCourseCommentDao.onGood(id);
        return JsonResult.success("点赞成功");
    }

    public JsonResult listComment(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cid"));
        String phone = JsonTools.getHeaderParams(params, "phone");

        Integer page = 0;
        try { page = Integer.parseInt(JsonTools.getJsonParam(params, "page")); } catch (Exception e) { } //页码

        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder();
        ssb.add(new SpecificationOperator("courseId", "eq", cid));
        ssb.add(new SpecificationOperator("status", "eq", "1", "and", new SpecificationOperator("phone", "eq", phone, "or")));
        Page<AppCourseComment> commentList = appCourseCommentDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        return JsonResult.success().set("commentList", commentList.getContent());
    }

    /**
     * 添加课程评论
     * @param params
     * @return
     */
    public JsonResult addComment(String params) {
        String phone = JsonTools.getHeaderParams(params, "phone");
        String content = JsonTools.getJsonParam(params, "content");
        Integer courseId = Integer.parseInt(JsonTools.getJsonParam(params, "courseId"));
        String courseTitle = JsonTools.getJsonParam(params, "courseTitle");
        AppCourseComment acc = new AppCourseComment();
        acc.setContent(content);
        acc.setCourseId(courseId);
        acc.setCourseTitle(courseTitle);
        acc.setCreateDay(NormalTools.curDate());
        acc.setCreateLong(System.currentTimeMillis());
        acc.setCreateTime(NormalTools.curDatetime());
        acc.setPhone(phone);
        Teacher tea = teacherDao.findByPhone(phone);
        if(tea!=null) {
            acc.setName(tea.getName());
            acc.setSchId(tea.getSchoolId());
            acc.setSchName(tea.getSchoolName());
        }
        appCourseCommentDao.save(acc);
        return JsonResult.success().set("obj", acc);
    }
}
