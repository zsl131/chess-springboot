package com.zslin.bus.yard.tools;

import com.zslin.basic.dao.IRoleDao;
import com.zslin.basic.dao.IUserDao;
import com.zslin.basic.dao.IUserRoleDao;
import com.zslin.basic.model.Role;
import com.zslin.basic.model.User;
import com.zslin.basic.model.UserRole;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

/**
 * 老师权限处理工具类
 * Created by zsl on 2018/10/16.
 */
@Component
public class TeacherRoleTools {

    public static final String TEACHER_ROLE = "SCHOOL_TEACHER_ROLE";

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IUserRoleDao userRoleDao;

    public void setTeacherRole(Teacher teacher) {
        String phone = teacher.getPhone();
        User u = userDao.findByUsername(phone); //通过手机号码作为用户名
        if(u==null) {
            u = new User();
            u.setNickname(teacher.getName());
            try {
                u.setPassword(SecurityUtil.md5(phone,phone.substring(phone.length()-6)));
            } catch (Exception e) {
            }
            u.setIsAdmin("0");
            u.setStatus(1);
            u.setUsername(phone);
            u.setPhone(phone);
            u.setCreateDate(NormalTools.curDatetime());
            userDao.save(u); //添加用户

            Role r = roleDao.findBySn(TEACHER_ROLE);
            if(r==null) {
                r = new Role();
                r.setSn(TEACHER_ROLE);
                r.setName("学校教师角色");
                roleDao.save(r);
            }

            UserRole ur = new UserRole();
            ur.setRid(r.getId());
            ur.setUid(u.getId());
            userRoleDao.save(ur); //分配角色
        }
    }
}
