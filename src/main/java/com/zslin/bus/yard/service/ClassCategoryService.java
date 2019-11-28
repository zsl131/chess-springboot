package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassCategoryDao;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dto.CategoryDto;
import com.zslin.bus.yard.dto.CategoryTreeDto;
import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.tools.CategoryTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
@Service
@AdminAuth(name = "课程分类管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/classCategory")
public class ClassCategoryService {

    @Autowired
    private IClassCategoryDao classCategoryDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private CategoryTools categoryTools;

    /** 用于为课程体系设置对应课程 */
    public JsonResult buildCategorySystemTree(String params) {
        Integer sid = Integer.parseInt(JsonTools.getJsonParam(params, "sid")); //System ID
        return JsonResult.success().set("data", categoryTools.buildCourseTree(sid));
    }

    /** 获取分类树结构 */
    public JsonResult buildCategoryTree(String params) {
        List<CategoryDto> list = categoryTools.buildCategoryTree();
        return JsonResult.success().set("data", list);
    }

    public JsonResult listRoot(String params) {
        Integer pid = 0;
        try { pid = Integer.parseInt(JsonTools.getJsonParam(params, "pid"));} catch (Exception e) {pid=0;}
        List<CategoryDto> list = categoryTools.buildCategoryTree();
        Sort sort = SimpleSortBuilder.generateSort("orderNo_a");
        List<ClassCategory> categoryList ;
        if(pid==0) {
            categoryList = classCategoryDao.findRoot(sort);
        } else {
            categoryList = classCategoryDao.findByParent(pid, sort);
        }
        CategoryTreeDto ctd = new CategoryTreeDto(list, categoryList);
        return JsonResult.getInstance().set("size", categoryList.size()).set("datas", ctd);
    }

    /**
     *
     * @param params {pid: 1}
     * @return
     */
    public JsonResult listChildren(String params) {
        Integer pid = 0;
        try { pid = Integer.parseInt(JsonTools.getJsonParam(params, "pid"));} catch (Exception e) {pid=0;}
        Sort sort = SimpleSortBuilder.generateSort("orderNo_a");
        List<ClassCategory> categoryList ;
        if(pid==0) {
            categoryList = classCategoryDao.findRoot(sort);
        } else {
            categoryList = classCategoryDao.findByParent(pid, sort);
        }

        return JsonResult.getInstance().set("size", categoryList.size()).set("datas", categoryList);
    }

    public JsonResult add(String params) {
        try {
            ClassCategory menu = JSONObject.toJavaObject(JSON.parseObject(params), ClassCategory.class);
            try {
                ClassCategory p = classCategoryDao.findOne(menu.getPid());
                if(p!=null) {
                    menu.setPname(p.getName());
                }
            } catch (Exception e) {
            }
            classCategoryDao.save(menu);
            return JsonResult.succ(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult update(String params) {
        try {
            ClassCategory menu = JSONObject.toJavaObject(JSON.parseObject(params), ClassCategory.class);
            ClassCategory m = classCategoryDao.findOne(menu.getId());
            m.setOrderNo(menu.getOrderNo());
            m.setName(menu.getName());
//            m.setStatus(menu.getStatus());
            classCategoryDao.save(m);
            return JsonResult.succ(m);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            List<Integer> childrenIds = classCategoryDao.findChildrenIds(id);
            if(childrenIds.size()>0) {
                return JsonResult.error("存在子元素不可删除！");
            }
            List<Integer> ids = classCourseDao.findIdsByCategoryId(id);
            if(ids.size()>0) {
                return JsonResult.error("存在科普课程不可删除！");
            } else {
                classCategoryDao.delete(id);
                return JsonResult.success("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

}
