package com.zslin.bus.yard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.dao.IUserDao;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.model.*;
import com.zslin.bus.yard.tools.TeacherRoleTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/10.
 */
@Service
@AdminAuth(name = "老师管理", psn = "科普进校园", orderNum = 2, type = "1", url = "/yard/teacher")
public class TeacherService {

    @Autowired
    private ISchoolDao schoolDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private TeacherRoleTools teacherRoleTools;

    @Autowired
    private ITeacherRoleDao teacherRoleDao;

    @Autowired
    private IGradeRoleDao gradeRoleDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private ITeacherGradeDao teacherGradeDao;

    @Autowired
    private IGradeDao gradeDao;

    @Autowired
    private IUserDao userDao;

    /**
     * 设置是否是测试用户
     * @param params 必须包含 isTest  id
     * @return
     */
    public JsonResult setIsTest(String params) {
        try {
            Integer id = JsonTools.getId(params);
            String isTest = JsonTools.getJsonParam(params, "isTest");
            teacherDao.updateIsTest(isTest, id);
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 为教师设置角色
     * @param params 必须包含tid
     * @return
     */
    public JsonResult onSetGrade(String params) {
        Integer tid = Integer.parseInt(JsonTools.getJsonParam(params, "tid")); //
        List<Grade> gradeList = gradeDao.findAll();
        List<Integer> gids = teacherGradeDao.findGidsByTid(tid);
        return JsonResult.success().set("gradeList", gradeList).set("gids", gids);
    }

    /**
     * 为教师设置角色
     * @param params 必须包含tid  gid  flag
     * @return
     */
    public JsonResult setGrade(String params) {
        try {
            Integer tid = Integer.parseInt(JsonTools.getJsonParam(params, "tid"));
            Integer gid = Integer.parseInt(JsonTools.getJsonParam(params, "gid"));
            String flag = JsonTools.getJsonParam(params, "flag");
            if("0".equals(flag)) { //删除
                teacherGradeDao.deleteByTidAndGid(tid, gid);
            } else if("1".equals(flag)) { //添加
                TeacherGrade tg = teacherGradeDao.findByTidAndGid(tid, gid);
                if(tg==null) {
                    tg = new TeacherGrade();
                    tg.setTid(tid); tg.setGid(gid);
                    teacherGradeDao.save(tg);
                }
            }
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("操作失败："+e.getMessage());
        }
    }

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Teacher> res = teacherDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult queryGradeRole(String params) {
        Integer tid = Integer.parseInt(JsonTools.getJsonParam(params, "tid")); //老师ID
        List<GradeRole> roleList = gradeRoleDao.findAll();
        List<Integer> ridList = teacherRoleDao.findRoleId(tid);
        return JsonResult.success().set("roleList", roleList).set("ridList", ridList);
    }

    public JsonResult authRole(String params) {
        Integer rid = Integer.parseInt(JsonTools.getJsonParam(params, "rid"));
        Integer tid = Integer.parseInt(JsonTools.getJsonParam(params, "tid"));
        String checked = JsonTools.getJsonParam(params, "checked");
        if("1".equals(checked)) {
            TeacherRole tr = new TeacherRole();
            tr.setRid(rid);
            tr.setTeaId(tid);
            teacherRoleDao.save(tr);
            return JsonResult.success("授权成功");
        } else {
            teacherRoleDao.delete(tid, rid);
            return JsonResult.success("取消授权成功");
        }
    }

    /**
     * 获取对象
     * @param params {id:0}
     * @return
     */
    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Teacher t = teacherDao.findOne(id);
            return JsonResult.succ(t);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        Teacher obj = JSONObject.toJavaObject(JSON.parseObject(params), Teacher.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            Teacher s = teacherDao.findOne(obj.getId());
            MyBeanUtils.copyProperties(obj, s, "id", "schoolId", "schoolName", "openid", "nickname", "hasBind");
            teacherDao.save(s);
        } else {
            String phone = obj.getPhone();
            try {
                obj.setPassword(SecurityUtil.md5(phone,phone.substring(phone.length()-6)));
            } catch (Exception e) {
            }
            teacherDao.save(obj);
            teacherRoleTools.setTeacherRole(obj); //设置用户信息
            wxAccountDao.updateTypeByPhone(obj.getPhone(), "4"); //设置为教师用户
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
            //TODO 还应删除对应的用户信息
            teacherDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /** 初始化密码 */
    public JsonResult initPwd(String params) {
        try {
            Integer id = JsonTools.getId(params);
            String phone = JsonTools.getJsonParam(params, "phone");
            String pwd = SecurityUtil.md5(phone, phone.substring(phone.length()-6));
            teacherDao.updatePwd(pwd, id);
            userDao.updatePhone(pwd, phone);
            return JsonResult.success("初始化密码成功");
        } catch (Exception e) {
            return JsonResult.error("初始失败："+e.getMessage());
        }
    }
}
