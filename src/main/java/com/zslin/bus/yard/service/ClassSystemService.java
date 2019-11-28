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
import com.zslin.bus.yard.dto.SystemDetailDto;
import com.zslin.bus.yard.dto.SystemDetailTreeDto;
import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;
import com.zslin.bus.yard.tools.ClassSystemTools;
import com.zslin.bus.yard.tools.TeacherVideoCountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/9/12.
 */
@Service
@AdminAuth(name = "课程体系管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/classSystem")
public class ClassSystemService {

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private ClassSystemTools classSystemTools;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private TeacherVideoCountTools teacherVideoCountTools;

    @Autowired
    private IGradeRoleSystemDao gradeRoleSystemDao;

    /** 获取体系 */
    public JsonResult findSystem(String params) {
        Sort sort = SimpleSortBuilder.generateSort("orderNo_a");
        Integer pid = 0;
        try { pid = Integer.parseInt(JsonTools.getJsonParam(params, "pid")); } catch (Exception e) { }
        List<ClassSystem> list ;
        if(pid!=null && pid>0) {
            list = classSystemDao.findByParent(pid, sort);
        } else {list = classSystemDao.findRoot(sort);}
        return JsonResult.success().set("list", list);
    }

    public JsonResult index(String params) {
        JsonResult result = JsonResult.success();
        String type = null;
        Integer id = 0;
        try {
            String pid = JsonTools.getJsonParam(params, "pid");
            String [] array = pid.split("_");
            type = array[0];
            id = Integer.parseInt(array[1]);
        } catch (Exception e) {
        }

        if("root".equalsIgnoreCase(type)) {
            List<ClassSystem> list = classSystemDao.findByParent(id, SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("system", classSystemDao.findOne(id)).set("type", type);
        } else if("child".equalsIgnoreCase(type)) {
            List<ClassSystemDetail> list = classSystemDetailDao.findBySid(id,SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("system", classSystemDao.findOne(id)).set("type", type);
        } else if("detail".equalsIgnoreCase(type)) {
            ClassSystemDetail obj = classSystemDetailDao.findOne(id);
            Integer courseId = obj.getCourseId();
            if(courseId!=null && courseId>0) {
                ClassCourse s = classCourseDao.findOne(courseId);
                if (s.getLearnId() != null && s.getLearnId() > 0) {
                    result.set("learn", attachmentDao.findOne(s.getLearnId()));
                } else {result.set("learn", "");}
                if (s.getPptId() != null && s.getPptId() > 0) {
                    result.set("ppt", attachmentDao.findOne(s.getPptId()));
                } else {result.set("ppt", "");}
                if (s.getVideoId() != null && s.getVideoId() > 0) {
                    result.set("video", attachmentDao.findOne(s.getVideoId()));
                } else {result.set("video", "");}
                result.set("course", s);
            } else {result.set("course", "");}
            result.set("system", "").set("obj", obj).set("type", type);
        } else {
            List<ClassSystem> list = classSystemDao.findRoot(SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("system", "").set("type", "base");
        }

        List<SystemDetailTreeDto> treeData = classSystemTools.buildSystemTree();
        result.set("treeData", treeData);
        return result;
    }

    public JsonResult indexByTeacher(String params) {
        String username = JsonTools.getHeaderParams(params, "username");
        JsonResult result = JsonResult.success();
        String type = null;
        Integer id = 0;
        try {
            String pid = JsonTools.getJsonParam(params, "pid");
            String [] array = pid.split("_");
            type = array[0];
            id = Integer.parseInt(array[1]);
        } catch (Exception e) {
        }

        if("root".equalsIgnoreCase(type)) {
            List<ClassSystem> list = classSystemDao.findByParent(id, SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("system", classSystemDao.findOne(id)).set("type", type);
        } else if("child".equalsIgnoreCase(type)) {
            List<ClassSystemDetail> list = classSystemDetailDao.findBySid(id,SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("system", classSystemDao.findOne(id)).set("type", type);
        } else if("detail".equalsIgnoreCase(type)) {
            ClassSystemDetail obj = classSystemDetailDao.findOne(id);
            Integer courseId = obj.getCourseId();
            if(courseId!=null && courseId>0) {
                ClassCourse s = classCourseDao.findOne(courseId);
                if (s.getLearnId() != null && s.getLearnId() > 0) {
                    result.set("learn", attachmentDao.findOne(s.getLearnId()));
                }else{result.set("learn", "");}
                if (s.getPptId() != null && s.getPptId() > 0) {
                    result.set("ppt", attachmentDao.findOne(s.getPptId()));
                }else {result.set("ppt", "");}
                if (s.getVideoId() != null && s.getVideoId() > 0) {
                    result.set("video", attachmentDao.findOne(s.getVideoId()));
                } else {result.set("video", "");}
                result.set("course", s).set("surplusCount", teacherVideoCountTools.getSurplusCount(username, courseId));
            } else {result.set("course", "");}
            result.set("system", "").set("obj", obj).set("type", type);
        } else {
            List<ClassSystem> list = classSystemDao.findRoot(SimpleSortBuilder.generateSort("orderNo"));
            result.set("data", list).set("system", "").set("type", "base");
        }

        List<SystemDetailTreeDto> treeData = classSystemTools.buildUserSystemTreeByRole(username);
        result.set("treeData", treeData);
        return result;
    }

    public JsonResult loadDetailByWxTeacher(String params) {
        String phone = JsonTools.getJsonParam(params, "phone");
        Integer id = JsonTools.getId(params);
        JsonResult result = JsonResult.success();
        ClassSystemDetail obj = classSystemDetailDao.findOne(id);
        Integer courseId = obj.getCourseId();
        if(courseId!=null && courseId>0) {
            ClassCourse s = classCourseDao.findOne(courseId);
            if (s.getLearnId() != null && s.getLearnId() > 0) {
                result.set("learn", attachmentDao.findOne(s.getLearnId()));
            }else{result.set("learn", "");}
            if (s.getPptId() != null && s.getPptId() > 0) {
                result.set("ppt", attachmentDao.findOne(s.getPptId()));
            }else {result.set("ppt", "");}
            if (s.getVideoId() != null && s.getVideoId() > 0) {
                result.set("video", attachmentDao.findOne(s.getVideoId()));
            } else {result.set("video", "");}
            result.set("course", s).set("surplusCount", teacherVideoCountTools.getSurplusCount(phone, courseId));
        } else {result.set("course", "");}
        return result.set("system", "").set("obj", obj);
    }

    /** 用于教师在微信端访问时调用 */
    public JsonResult listByWxTeacher(String params) {
        JsonResult result = JsonResult.getInstance();
        String phone = JsonTools.getJsonParam(params, "phone");
        /*Integer sid ;
        try { sid = Integer.parseInt(JsonTools.getJsonParam(params, "sid")); } catch (NumberFormatException e) { sid = 0; }*/
        List<Integer> systemIds = gradeRoleSystemDao.findSystemIdByPhone(phone);
        if(systemIds==null || systemIds.size()<=0) {return result;}
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<ClassSystem> systemList = classSystemDao.findByIds(systemIds, sort);

        List<SystemDetailDto> dtoList = new ArrayList<>();

        for(ClassSystem cs : systemList) {
            dtoList.add(new SystemDetailDto(cs, classSystemDetailDao.findBySid(cs.getId(), sort)));
        }
        /*if(sid>0) {
            List<ClassSystemDetail> detailList = classSystemDetailDao.findBySid(sid, sort);
            result.set("detailList", detailList);
        } else if(systemList!=null && systemList.size()>0) {
            List<ClassSystemDetail> detailList = classSystemDetailDao.findBySid(systemList.get(0).getId(), sort);
            result.set("detailList", detailList);
        } else {

        }*/
        return result.set("dtoList", dtoList);
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ClassSystem> res = classSystemDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    /**
     * 不分页，用于添加体系内容
     * @param params
     * @return
     */
    public JsonResult listNoPage(String params) {
        List<ClassSystem> list = classSystemDao.findAll();
        return JsonResult.getInstance().set("size", list.size()).set("list", list);
    }

    /**
     * 获取对象
     * @param params {id:0}
     * @return
     */
    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            ClassSystem s = classSystemDao.findOne(id);
            return JsonResult.succ(s);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        ClassSystem obj = JSONObject.toJavaObject(JSON.parseObject(params), ClassSystem.class);
        try {
            ClassSystem p = classSystemDao.findOne(obj.getPid());
            if(p!=null) {
                obj.setPname(p.getName());
            }
        } catch (Exception e) {
        }
        if(obj.getId()!=null && obj.getId()>0) { //修改
            ClassSystem s = classSystemDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s, "id");
            classSystemDao.save(s);
        } else {
            classSystemDao.save(obj);
        }
        return JsonResult.succ(obj);
    }

    /**
     * 删除对象
     * @param params {id:0}
     * @return
     */
    public JsonResult deleteSystem(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            List<Integer> sonIds = classSystemDao.findChildren(id);
            List<Integer> detailIds = classSystemDetailDao.findIdsBySid(id);
            if(sonIds.size()>0 || detailIds.size()>0) {
                return JsonResult.error("存在子元素不可删除");
            } else {
                classSystemDao.delete(id);
                return JsonResult.success("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
