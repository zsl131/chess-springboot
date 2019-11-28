package com.zslin.bus.yard.service;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IAttachmentDao;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.model.Attachment;
import com.zslin.bus.yard.model.ClassCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by zsl on 2018/9/13.
 */
@Service
public class AttachmentService {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    /**
     * 获取附件
     * @param params 必须包含cid
     * @return
     */
    public JsonResult loadAttach(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cid")); //Course ID
        ClassCourse cc = classCourseDao.findOne(cid);
        Attachment ap = (cc.getPptId()!=null && cc.getPptId()>0)?attachmentDao.findOne(cc.getPptId()):null;
        Attachment av = (cc.getVideoId()!=null && cc.getVideoId()>0)?attachmentDao.findOne(cc.getVideoId()):null;
        Attachment al = (cc.getLearnId()!=null && cc.getLearnId()>0)?attachmentDao.findOne(cc.getLearnId()):null;
        return JsonResult.success().set("ppt", ap).set("video", av).set("learn", al);
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Attachment a = attachmentDao.findOne(id);
            return JsonResult.succ(a);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     *
     * @param params {id:0, courseId:1, fieldName:pptId}
     * @return
     */
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String fieldName = JsonTools.getJsonParam(params, "fieldName");
            if(fieldName!=null && !"".equals(fieldName.trim())) {
                Integer courseId = Integer.parseInt(JsonTools.getJsonParam(params, "courseId"));
                if(fieldName.contains("ppt")) {classCourseDao.cleanPPT(courseId);}
                else if(fieldName.contains("video")) {classCourseDao.cleanVideo(courseId);}
                else if(fieldName.contains("learn")) {classCourseDao.cleanLearn(courseId);}
            }
            Attachment a = attachmentDao.findOne(id);
            File file = new File(configTools.getUploadPath() + a.getUrl());
            if(file!=null && file.exists()) {
                file.delete();
            }
            attachmentDao.delete(a);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("删除失败");
        }
    }

    /**
     *
     * @param params {ids: '1,0,'}
     * @return
     */
    public JsonResult deleteByIds(String params) {
        try {
            String ids = JsonTools.getJsonParam(params, "ids");
            for(String idStr : ids.split(",")) {
                if(idStr!=null && !"".equals(idStr)) {
                    Integer id = Integer.parseInt(idStr.trim());
                    Attachment a = attachmentDao.findOne(id);
                    File file = new File(configTools.getUploadPath() + a.getUrl());
                    if(file!=null && file.exists()) {
                        file.delete();
                    }
                    attachmentDao.delete(a);
                }
            }
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
