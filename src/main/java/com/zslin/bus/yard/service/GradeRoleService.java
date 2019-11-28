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
import com.zslin.bus.yard.dao.IClassSystemDao;
import com.zslin.bus.yard.dao.IGradeRoleDao;
import com.zslin.bus.yard.dao.IGradeRoleSystemDao;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.GradeRole;
import com.zslin.bus.yard.model.GradeRoleSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2019/2/25.
 */
@Service
@AdminAuth(name = "年级角色管理", psn = "科普进校园", orderNum = 3, type = "1", url = "/yard/gradeRole")
public class GradeRoleService {

    @Autowired
    private IGradeRoleDao gradeRoleDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IGradeRoleSystemDao gradeRoleSystemDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<GradeRole> res = gradeRoleDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult querySystem(String params) {
        Integer rid = Integer.parseInt(JsonTools.getJsonParam(params, "rid")); //RoleId
        Sort sort = SimpleSortBuilder.generateSort("pid", "orderNo");
        List<ClassSystem> systemList = classSystemDao.findHasParent(sort);
        List<Integer> sidList = gradeRoleSystemDao.findSystemId(rid);
        return JsonResult.success().set("systemList", systemList).set("sidList", sidList);
    }

    /**
     * 不分页，用于添加课程
     * @param params
     * @return
     */
    public JsonResult listNoPage(String params) {
        List<GradeRole> list = gradeRoleDao.findAll();
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
            GradeRole o = gradeRoleDao.findOne(id);
            return JsonResult.succ(o);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        GradeRole obj = JSONObject.toJavaObject(JSON.parseObject(params), GradeRole.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            GradeRole s = gradeRoleDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s);
            gradeRoleDao.save(s);
        } else {
            gradeRoleDao.save(obj);
        }
        return JsonResult.succ(obj);
    }

    public JsonResult authSystem(String params) {
        Integer rid = Integer.parseInt(JsonTools.getJsonParam(params, "rid"));
        Integer sid = Integer.parseInt(JsonTools.getJsonParam(params, "sid"));
        String checked = JsonTools.getJsonParam(params, "checked");
        if("1".equals(checked)) {
            GradeRoleSystem grs = new GradeRoleSystem();
            grs.setRid(rid);
            grs.setSid(sid);
            gradeRoleSystemDao.save(grs);
            return JsonResult.success("授权成功");
        } else {
            gradeRoleSystemDao.delete(rid, sid);
            return JsonResult.success("取消授权成功");
        }
    }

    /**
     * 删除对象
     * @param params {id:0}
     * @return
     */
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            gradeRoleDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
