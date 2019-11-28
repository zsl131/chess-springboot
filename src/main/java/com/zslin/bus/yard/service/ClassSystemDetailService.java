package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassSystemDao;
import com.zslin.bus.yard.dao.IClassSystemDetailDao;
import com.zslin.bus.yard.dao.ISystemCourseDao;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;
import com.zslin.bus.yard.model.SystemCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/12/24.
 */
@Service
public class ClassSystemDetailService {

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private ISystemCourseDao systemCourseDao;

    public JsonResult addOrUpdate(String params) {
//        Integer sid = Integer.parseInt(JsonTools.getJsonParam(params, "sid")); //System id
        ClassSystemDetail obj = JSONObject.toJavaObject(JSON.parseObject(params), ClassSystemDetail.class);
        try {
            ClassSystem cs = classSystemDao.findOne(obj.getSid());
            obj.setSname(cs.getName());
            obj.setSpid(cs.getPid());
            obj.setSpname(cs.getPname());
        } catch (Exception e) {
        }
        try {
            ClassCourse cc = classCourseDao.findOne(obj.getCourseId());
            obj.setCourseTarget(cc.getLearnTarget());
            obj.setCourseTitle(cc.getTitle());
            obj.setCname(cc.getCname());
            obj.setCpname(cc.getCpname());
        } catch (Exception e) {
        }
        if(obj.getId()!=null &&obj.getId()>0) { //修改
            ClassSystemDetail csd = classSystemDetailDao.findOne(obj.getId());
            deleteSystemCourse(csd.getCourseId(), csd.getSid()); //先删除对应关系
            csd.setName(obj.getName());
            csd.setCourseTitle(obj.getCourseTitle());
            csd.setCourseTarget(obj.getCourseTarget());
            csd.setCourseId(obj.getCourseId());
            csd.setOrderNo(obj.getOrderNo());
            csd.setSectionNo(obj.getSectionNo());
            csd.setInRange(obj.getInRange());
            addSystemCourse(csd.getCourseId(), csd.getSid()); //再添加对应关系
            classSystemDetailDao.save(csd);
        } else { //添加
            addSystemCourse(obj.getCourseId(), obj.getSid());
            classSystemDetailDao.save(obj);
        }
        return JsonResult.success("保存成功");
    }

    private void deleteSystemCourse(Integer cid, Integer sid) {
        if(cid!=null &&cid>0 && sid!=null && sid>0) {
            systemCourseDao.deleteBySidAndCid(sid, cid);
        }
    }

    private void addSystemCourse(Integer cid, Integer sid) {
        if(cid!=null &&cid>0 && sid!=null && sid>0) {
            SystemCourse sc = new SystemCourse();
            sc.setCid(cid);
            sc.setSid(sid);
            systemCourseDao.save(sc);
        }
    }

    public JsonResult deleteSystemDetail(String params) {
        try {
            Integer id = JsonTools.getId(params);
            classSystemDetailDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
