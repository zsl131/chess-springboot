package com.zslin.bus.app.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppFeedbackDao;
import com.zslin.bus.app.dao.IAppFeedbackImgDao;
import com.zslin.bus.app.model.AppFeedback;
import com.zslin.bus.app.model.AppFeedbackImg;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2019/9/24.
 */
@Service
@AdminAuth(name = "移动评论管理", psn = "移动端管理", orderNum = 3, type = "1", url = "/admin/appFeedback")
public class AppFeedbackService {

    @Autowired
    private IAppFeedbackDao appFeedbackDao;

    @Autowired
    private IAppFeedbackImgDao appFeedbackImgDao;

    @Autowired
    private ITeacherDao teacherDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<AppFeedback> list = appFeedbackDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
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
            AppFeedback af = appFeedbackDao.findOne(id);
            af.setReply(reply);
            af.setReplyLong(System.currentTimeMillis());
            af.setReplyTime(NormalTools.curDatetime());
            af.setReplyDay(NormalTools.curDate());
            af.setStatus(status);
            appFeedbackDao.save(af);
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
        AppFeedback af = appFeedbackDao.findOne(id);
        List<AppFeedbackImg> imgList = appFeedbackImgDao.findByRandomId(af.getRandomId());
        return JsonResult.succ(af).set("imgList", imgList);
    }

    public JsonResult addFeedback(String params) {
        try {
            String phone = JsonTools.getHeaderParams(params, "phone");
            String randomId = JsonTools.getJsonParam(params, "randomId");
            String content = JsonTools.getJsonParam(params, "content");
            String contact = JsonTools.getJsonParam(params, "contact");
            Integer score = Integer.parseInt(JsonTools.getJsonParam(params, "score"));
            AppFeedback af = new AppFeedback();
            Teacher tea = teacherDao.findByPhone(phone);
            if(tea!=null) {
                af.setSchName(tea.getSchoolName());
                af.setSchId(tea.getSchoolId());
                af.setTeaName(tea.getName());
            }
            af.setRandomId(randomId);
            af.setContact(contact);
            af.setContent(content);
            af.setPhone(phone);
            af.setScore(score);
            af.setCreateDay(NormalTools.curDate());
            af.setCreateTime(NormalTools.curDatetime());
            af.setCreateLong(System.currentTimeMillis());
            appFeedbackDao.save(af);
            appFeedbackImgDao.updateFlag("1", randomId); //把对应的图片设置为有用
            return JsonResult.success().set("feedback", af);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
