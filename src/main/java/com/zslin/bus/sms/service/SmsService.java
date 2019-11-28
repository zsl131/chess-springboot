package com.zslin.bus.sms.service;

import com.zslin.basic.dao.IUserDao;
import com.zslin.bus.basic.dao.IRelationDao;
import com.zslin.bus.basic.model.OtherLogin;
import com.zslin.bus.basic.model.Relation;
import com.zslin.bus.basic.tools.OtherLoginTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.RandomTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.tools.SmsTools;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.model.WxAccount;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/9/26.
 */
@Service
public class SmsService {

    @Autowired
    private SmsTools smsTools;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRelationDao relationDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private OtherLoginTools otherLoginTools;

    @Autowired
    private ITeacherDao teacherDao;

    /**
     * 后台登陆发送验证码
     * @param params {phone:''}
     * @return
     */
    public JsonResult sendCodeByLogin(String params) {
        try {
            String phone = JsonTools.getJsonParam(params, "phone");
            Relation r = relationDao.findByPhone(phone);
            if(r==null) { //
                return JsonResult.success("未检测到对应用户").set("error", "1");
            } else if(r.getUsername()==null || "".equals(r.getUsername())) {
                return JsonResult.success("该手机号码未绑定用户").set("error","1");
            }
            String code = RandomTools.genCode4();
            smsTools.sendMsg(phone, "code", code);
            OtherLogin login = otherLoginTools.onLogin(r.getUsername());
            return JsonResult.success("发送成功").set("code", code).set("phone", phone).set("username", r.getUsername()).set("token", login.getToken());
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 后台绑定手机号码
     * @param params {phone:''}
     * @return
     */
    public JsonResult sendCode(String params) {
        try {
            String phone = JsonTools.getJsonParam(params, "phone");
            Relation r = relationDao.findByPhone(phone);
            if(r!=null && r.getUsername()!=null && !"".equals(r.getUsername())) { //如果该手机号码已经绑定用户名
                return JsonResult.error("该手机已绑定用户："+r.getUsername());
            }
            String code = RandomTools.genCode4();
            smsTools.sendMsg(phone, "code", code);
            return JsonResult.success("发送成功").set("code", code).set("phone", phone);
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 微信平台绑定手机号码
     * @param params {phone:''}
     * @return
     */
    public JsonResult loadCode4WxBind(String params) {
        try {
            String phone = JsonTools.getJsonParam(params, "phone");
            Relation r = relationDao.findByPhone(phone);
            if(r!=null && r.getOpenid()!=null && !"".equals(r.getOpenid())) { //如果该手机号码已经绑定用户名
                return JsonResult.error("该手机已绑定微信用户");
            }
            String code = RandomTools.genCode4();
            smsTools.sendMsg(phone, "code", code);
            return JsonResult.success("发送成功").set("code", code).set("phone", phone);
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 通过Openid绑定手机号码
     * @param params {phone:'1', openid:'11'}
     * @return
     */
    public JsonResult bindPhoneByOpenid(String params) {
        try {
            String phone = JsonTools.getJsonParam(params, "phone");
            String openid = JsonTools.getJsonParam(params, "openid");
            if(phone==null || "".equals(phone) || openid ==null || "".equals(openid)) {
                return JsonResult.error("用户名或手机号码为空");
            }
            Relation ur = relationDao.findByOpenid(openid);
            Relation pr = relationDao.findByPhone(phone);
            if(ur==null && pr ==null) { // 如果不存在，则直接保存
                ur = new Relation();
                ur.setPhone(phone); ur.setOpenid(openid);
                relationDao.save(ur);
                wxAccountDao.modifyPhone(phone, openid);
                modifyTeacher(phone);
            } else if(ur!=null && pr==null) { //如果对应用户关系存在，且已绑定手机号码
                if(ur.getPhone()!=null && !"".equals(ur.getPhone())){
                    return JsonResult.error("您已绑定手机" + ur.getPhone());
                } else {
                    ur.setPhone(phone);
                    relationDao.save(ur);
//                    userDao.updatePhone(username, phone);
                    wxAccountDao.modifyPhone(phone, openid);
                    modifyTeacher(phone);
                }
            } else if(pr!=null) {
                if(pr.getUsername()!=null && !"".equals(pr.getUsername())) { //如果有用户名
//                    return JsonResult.error("手机号已绑定用户："+username);
                    userDao.updatePhone(pr.getUsername(), phone);
                    modifyTeacher(phone);
                }
                if(pr.getOpenid()!=null && !"".equals(pr.getOpenid())) {
                    return JsonResult.error("手机号码已被他人绑定");
                } else {
                    pr.setOpenid(openid);
                    relationDao.save(pr);
                    wxAccountDao.modifyPhone(phone, openid);
                    modifyTeacher(phone);
                }
            }
            return JsonResult.success("绑定成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    private void modifyTeacher(String phone) {
        Teacher t = teacherDao.findByPhone(phone);
        if(t!=null && !"1".equals(t.getHasBind())) {
            WxAccount account = wxAccountDao.findByPhone(phone);
            t.setHasBind("1");
            t.setNickname(account.getNickname());
            t.setOpenid(account.getOpenid());
            teacherDao.save(t);
            wxAccountDao.updateTypeByPhone(phone, "4"); //设置为科普教师
        }
    }

    /**
     * 通过用户名绑定手机号码
     * @param params {phone:'11', username:'root'}
     * @return
     */
    public JsonResult bindPhoneByUsername(String params) {
        try {
            String phone = JsonTools.getJsonParam(params, "phone");
            String username = JsonTools.getJsonParam(params, "username");
            if(phone==null || "".equals(phone) || username ==null || "".equals(username)) {
                return JsonResult.error("用户名或手机号码为空");
            }
            Relation ur = relationDao.findByUsername(username);
            Relation pr = relationDao.findByPhone(phone);
            if(ur==null && pr ==null) { // 如果不存在，则直接保存
                ur = new Relation();
                ur.setPhone(phone); ur.setUsername(username);
                relationDao.save(ur);
                userDao.updatePhone(username, phone);
            } else if(ur!=null) { //如果对应用户关系存在，且已绑定手机号码
                if(ur.getPhone()!=null && !"".equals(ur.getPhone())){
                    return JsonResult.error("您已绑定手机" + ur.getPhone());
                } else {
                    ur.setPhone(phone);
                    relationDao.save(ur);
                    userDao.updatePhone(username, phone);
                }
            } else if(pr!=null) {
                if(pr.getOpenid()!=null && !"".equals(pr.getOpenid())) {
                    wxAccountDao.modifyPhone(phone, pr.getOpenid());
                }
                if(pr.getUsername()!=null && !"".equals(pr.getUsername())) {
                    return JsonResult.error("手机号已绑定用户："+username);
                } else {
                    pr.setUsername(username);
                    relationDao.save(pr);
                    userDao.updatePhone(username, phone);
                }
            }
            return JsonResult.success("绑定成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
