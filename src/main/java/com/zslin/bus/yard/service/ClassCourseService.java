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
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.model.Attachment;
import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassCourseAtta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/12.
 */
@Service
@AdminAuth(name = "课程管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/course")
public class ClassCourseService {

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IGradeDao gradeDao;

    @Autowired
    private IClassCategoryDao classCategoryDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private IClassCourseAttaDao classCourseAttaDao;

    /** 重新构建视频ID */
    public JsonResult rebuildVideoIds(String params) {
        Integer id = JsonTools.getId(params); //ClassCourse的ID
        Integer attaId = JsonTools.getIntegerParams(params, "attaId"); //Attachment的ID
        String flag = JsonTools.getJsonParam(params, "flag"); //如果为空或1，则添加；其他则为删除
        ClassCourse cc = classCourseDao.findOne(id);
        cc.setVideoIds(handle(cc.getVideoIds(), attaId, flag));
        classCourseDao.save(cc);
        return JsonResult.success("操作成功");
    }

    /** 处理IDs */
    private String handle(String ids, Integer attaId, String flag) {
//        String ids = cc.getVideoIds();
        if(ids==null) {ids = ",";} //先初始化
        if(flag == null || "1".equals(flag)) { //如果为空或为1，都是添加
            ids += attaId+",";
        } else {
            ids = ids.replace(","+attaId+",", ",");
        }
        //cc.setVideoIds(ids);
        return ids;
    }

    /** 获取课程对应的附件信息 */
    public JsonResult handleAtta(String params) {
        Integer id = JsonTools.getId(params);
        ClassCourse s = classCourseDao.findOne(id);
        JsonResult result = JsonResult.succ(s);
        if(s.getLearnId()!=null && s.getLearnId()>0) {result.set("learn", attachmentDao.findOne(s.getLearnId()));}
        if(s.getPptId()!=null && s.getPptId()>0) {result.set("ppt", attachmentDao.findOne(s.getPptId()));}
        if(s.getVideoId()!=null && s.getVideoId()>0) {result.set("video", attachmentDao.findOne(s.getVideoId()));}
        String videoIds = s.getVideoIds();
        videoIds = videoIds==null?"0":"0"+videoIds+"0";
//        System.out.println("-----------ClassCourseService.handleAtta---------"+videoIds);
        List<Attachment> attaList = attachmentDao.listByHql("FROM Attachment a WHERE a.id IN ("+videoIds+")");

        result.set("attachmentList", attaList);
        return result;
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ClassCourse> res = classCourseDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 获取对象
     * @param params {id:0}
     * @return
     */
    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            ClassCourse s = classCourseDao.findOne(id);
            JsonResult result = JsonResult.succ(s);
            if(s.getLearnId()!=null && s.getLearnId()>0) {result.set("learn", attachmentDao.findOne(s.getLearnId()));}
            if(s.getPptId()!=null && s.getPptId()>0) {result.set("ppt", attachmentDao.findOne(s.getPptId()));}
            if(s.getVideoId()!=null && s.getVideoId()>0) {result.set("video", attachmentDao.findOne(s.getVideoId()));}
            List<ClassCourseAtta> attaList = classCourseAttaDao.findByCourseId(id);
            result.set("gradeList", gradeDao.findAll(SimpleSortBuilder.generateSort("orderNo_a")))
            .set("attaList", attaList);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        ClassCourse obj = JSONObject.toJavaObject(JSON.parseObject(params), ClassCourse.class);
        Integer cid = obj.getCid();
        ClassCategory cate = classCategoryDao.findOne(cid);
        if(cate!=null) {
            obj.setCname(cate.getName());
            obj.setCpid(cate.getPid());
            obj.setCpname(cate.getPname());
        }
        obj.setGrade(gradeDao.findNameById(obj.getGradeId())); //设置年级
        if(obj.getId()!=null && obj.getId()>0) { //修改
            ClassCourse s = classCourseDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s, "id", "showTest");
            classCourseDao.save(s);
            //修改体系对应的课程内容
            classSystemDetailDao.updateCourse(s.getLearnTarget(), s.getTitle(), s.getId());
        } else {
            classCourseDao.save(obj);
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
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            List<Integer> detailIds = classSystemDetailDao.findIdsByCourseId(id);
            if(detailIds.size()>0) {
                return JsonResult.error("已经在体系中绑定，不可删除");
            } else {
                classCourseDao.delete(id);
                return JsonResult.success("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 设置课程是否给测试用户看
     * @param params 必须包含 showTest  id
     * @return
     */
    public JsonResult setShowTest(String params) {
        try {
            Integer id = JsonTools.getId(params);
            String showTest = JsonTools.getJsonParam(params, "showTest");
            classCourseDao.updateShowTest(showTest, id);
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
