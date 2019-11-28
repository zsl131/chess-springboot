package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.dao.IUserDao;
import com.zslin.basic.model.User;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.basic.dao.IDepUserDao;
import com.zslin.bus.basic.dao.IDepartmentDao;
import com.zslin.bus.basic.dto.DepUserDto;
import com.zslin.bus.basic.iservice.IDepartmentService;
import com.zslin.bus.basic.model.DepUser;
import com.zslin.bus.basic.model.Department;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/7/12.
 */
@Service
@AdminAuth(name = "部门管理", psn = "系统管理", type = "1", url = "/admin/department", orderNum = 2)
public class DepartmentService implements IDepartmentService {

    @Autowired
    private IDepartmentDao departmentDao;

    @Autowired
    private IDepUserDao depUserDao;

    @Autowired
    private IUserDao userDao;

    public JsonResult setDepUser(String params) {
        try {
            Integer userId = Integer.parseInt(JsonTools.getJsonParam(params, "userId"));
            Integer depId = Integer.parseInt(JsonTools.getJsonParam(params, "depId"));
            DepUser du = depUserDao.findByDepIdAndUserId(depId, userId);
            String message ;

            if(du==null) {
                du = new DepUser();
                User u = userDao.findOne(userId);
                du.setNickname(u.getNickname());
                du.setUsername(u.getUsername());
                du.setUserId(userId);

                Department d = departmentDao.findOne(depId);
                du.setDepName(d.getName());
                du.setDepId(depId);

                depUserDao.save(du);
                message = "设置用户成功";
            } else {
                depUserDao.delete(du);
                message = "取消用户权限成功";
            }

            return JsonResult.success(message);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult loadAuthUser(String params) {
//        System.out.println("params:::"+params);
        try {
            Integer depId = Integer.parseInt(JsonTools.getJsonParam(params, "depId"));
            List<Integer> userIds = depUserDao.findAuthUserIds(depId);
            List<User> userList = userDao.findAll();
            DepUserDto dud = new DepUserDto(userList, userIds);
            return JsonResult.succ(dud);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Department> res = departmentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Department dep = departmentDao.findOne(id);
            return JsonResult.succ(dep);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @AdminAuth(name = "添加或修改", orderNum = 1)
    public JsonResult addOrUpdate(String params) {
        try {
            Department dep = JSONObject.toJavaObject(JSON.parseObject(params), Department.class);
            if(dep.getId()==null || dep.getId()<=0) {
                departmentDao.save(dep);
            } else {
                Department d = departmentDao.findOne(dep.getId());
                d.setName(dep.getName());
                departmentDao.save(d);
            }
            return JsonResult.succ(dep);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    @AdminAuth(name = "删除", orderNum = 1)
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            departmentDao.delete(id);
            depUserDao.deleteByDepId(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
