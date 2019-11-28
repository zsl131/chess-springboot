package com.zslin.bus.yard.service;

import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassCourseTagDao;
import com.zslin.bus.yard.dao.IClassTagDao;
import com.zslin.bus.yard.model.ClassCourseTag;
import com.zslin.bus.yard.model.ClassTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2019/9/7.
 */
@Service
public class ClassCourseTagService {

    @Autowired
    private IClassTagDao classTagDao;

    @Autowired
    private IClassCourseTagDao classCourseTagDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    /**
     * 添加并获取Tag对象
     * @param params
     * @return
     */
    public JsonResult loadTag(String params) {
        String name = JsonTools.getJsonParam(params, "name"); //名称
        ClassTag ct = classTagDao.findByName(name);
        if(ct==null) { ct = new ClassTag(); ct.setName(name); classTagDao.save(ct);}
        return JsonResult.success("获取成功").set("tag", ct);
    }

    /**
     * 获取Course对应的Tag
     * @param params 必须包含CourseId
     * @return
     */
    public JsonResult loadTags(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cid")); //Course ID
        List<ClassTag> allTags = classTagDao.findAll();
        List<Integer> tids = classCourseTagDao.findTids(cid);
        return JsonResult.success().set("allTags", allTags).set("tids", tids);
    }

    /**
     * 为课程设置Tag
     * @param params 必须包含cid和tid
     * @return
     */
    public JsonResult setTag(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cid")); //Course ID
        Integer tid = Integer.parseInt(JsonTools.getJsonParam(params, "tid")); //Tag ID
        ClassCourseTag cct = classCourseTagDao.findByTidAndCid(tid, cid);
        if(cct==null) {
            cct = new ClassCourseTag();
            cct.setCid(cid); cct.setTid(tid); classCourseTagDao.save(cct);
        } else {
            classCourseTagDao.delete(cct);
        }
        resetTagName(cid); //重新设置标签名称
        return JsonResult.success();
    }

    private void resetTagName(Integer cid) {
        String tagName = buildTagName(cid);
        classCourseDao.updateTags(tagName, cid);
    }

    private String buildTagName(Integer cid) {
        List<Integer> tagIds = classCourseTagDao.findTids(cid);
        if(tagIds==null || tagIds.size()<=0) {return "";}
        else {
            StringBuffer sb = new StringBuffer();
            List<ClassTag> tagList = classTagDao.findByIds(tagIds);
            for(ClassTag c : tagList) {sb.append(c.getName()).append(",");}
            String res = sb.toString();
            if(res.endsWith(",")) {res = res.substring(0, res.length()-1);}
            return res;
        }
    }
}
