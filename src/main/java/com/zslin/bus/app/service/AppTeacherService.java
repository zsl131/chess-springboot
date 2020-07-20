package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.bus.common.tools.AppUserLoginTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassTag;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2019/8/22.
 */
@Service
public class AppTeacherService {

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private ITeacherRoleDao teacherRoleDao;

    @Autowired
    private ITeacherGradeDao teacherGradeDao;

    @Autowired
    private IClassTagDao classTagDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private AppUserLoginTools appUserLoginTools;

    /**
     * 获取教师所可访问的年级
     */
    public JsonResult loadGrade(String params) {
        String phone = JsonTools.getJsonParam(params, "phone"); //必须包含Phone
        String type = JsonTools.getJsonParam(params, "type"); //类型，即是否是标签
        Integer tagId = null;
        try {
            tagId = Integer.parseInt(JsonTools.getJsonParam(params, "tagId"));
        } catch (Exception e) {
        }
        String isTest = teacherDao.queryIsTest(phone); //是否为测试教师
        List<ClassSystem> list = null;
        if (type != null && "tag".equalsIgnoreCase(type)) {
            ClassTag tag = null;
            if (tagId != null && tagId > 0) {
                tag = classTagDao.findOne(tagId);
            }
            list = new ArrayList<>();
            ClassSystem g = new ClassSystem();
            g.setName(tag == null ? "标签课程" : "课程库[" + tag.getName() + "]");
            g.setId(-2);
            g.setOrderNo(-2);
            list.add(g);
        } else {
            if (!"1".equals(isTest)) { //不是测试教师
//                list = teacherGradeDao.findGradeByPhone(phone, SimpleSortBuilder.generateSort("orderNo"));
                list = classSystemDao.findByPhone(phone, SimpleSortBuilder.generateSort("orderNo"));
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            if ("1".equals(isTest)) { //如果是测试教师
                ClassSystem g = new ClassSystem();
                g.setName("测试教师用户");
                g.setId(-1);
                g.setOrderNo(-1);
                list.add(g);
            }
            ClassSystem g = new ClassSystem();
            g.setName("访问历史");
            g.setId(0);
            g.setOrderNo(0);
            list.add(g);
        }

        return JsonResult.success("获取成功").set("datas", list);
    }

    /**
     * 修改密码
     *
     * @param params 必须包含 id
     * @return
     */
    public JsonResult updatePwd(String params) {
        Integer id = JsonTools.getId(params);
        String oldPwd = JsonTools.getJsonParam(params, "oldPwd");
        String pwd = JsonTools.getJsonParam(params, "pwd");
        String rePwd = JsonTools.getJsonParam(params, "rePwd");
        Teacher tea = teacherDao.findOne(id);
        String phone = tea.getPhone();
        try {
            if (!pwd.equals(rePwd)) {
                return JsonResult.success("两次密码不一致").set("flag", "0");
            }
            oldPwd = SecurityUtil.md5(phone, oldPwd);
            if (!oldPwd.equals(tea.getPassword())) {
                return JsonResult.success("原始密码不正确").set("flag", "0");
            }
            rePwd = SecurityUtil.md5(phone, rePwd);
            teacherDao.updatePwd(rePwd, id);
            return JsonResult.success("密码修改成功").set("flag", "1");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * App 登陆
     */
    public JsonResult login(String params) {
        try {
            String username = JsonTools.getJsonParam(params, "username"); //即手机号码
            String password = JsonTools.getJsonParam(params, "password");
            String pwd = SecurityUtil.md5(username, password);
            Teacher t = teacherDao.findByPhone(username);
            if (t == null) {
                return JsonResult.success("用户不存在").set("flag", "0");
            } else if (t.getPassword() == null || !t.getPassword().equals(pwd)) { //密码为空或不匹配
                return JsonResult.success("密码不正确").set("flag", "0");
            } else {
                String token = appUserLoginTools.buildToken(username); //token值
                //System.out.println("------AppTeacherService-------"+token);
                return JsonResult.success("获取成功").set("flag", "1").set("obj", t).set("token", token);
            }
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
