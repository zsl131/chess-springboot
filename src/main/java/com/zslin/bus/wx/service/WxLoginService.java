package com.zslin.bus.wx.service;

import com.zslin.bus.basic.dao.IRelationDao;
import com.zslin.bus.basic.model.OtherLogin;
import com.zslin.bus.basic.model.Relation;
import com.zslin.bus.basic.tools.OtherLoginTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.RandomTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.dao.IWxLoginDao;
import com.zslin.bus.wx.model.WxAccount;
import com.zslin.bus.wx.model.WxLogin;
import com.zslin.bus.wx.tools.EventTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/9/25.
 */
@Service
public class WxLoginService {

    @Autowired
    private IWxLoginDao wxLoginDao;

    @Autowired
    private EventTools eventTools;

    @Autowired
    private IRelationDao relationDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private OtherLoginTools otherLoginTools;

    /**
     * 为用户绑定微信生成二维码
     * @param params {username: 'root'}
     * @return
     */
    public JsonResult buildQr4Bind(String params) {
        String username = JsonTools.getJsonParam(params, "username");
        Relation r = relationDao.findByUsername(username);
        if(r!=null && r.getOpenid()!=null && !"".equals(r.getOpenid())) { //已经绑定
            WxAccount account = wxAccountDao.findByOpenid(r.getOpenid());
            return JsonResult.success("已经绑定").set("hasBind", "1").set("obj", account);
        } else {
            String random = "bind_"+RandomTools.genTimeNo(5,5); //bind_开头表示是绑定用
            WxLogin wl = new WxLogin();
            wl.setCreateLong(System.currentTimeMillis()/1000); //秒
            wl.setToken(random);

            String qrStr = eventTools.getTempQr(random);
            if(qrStr!=null && !"".equals(qrStr)) {
                wl.setTicket(JsonTools.getJsonParam(qrStr, "ticket"));
            }

            wxLoginDao.save(wl); //保存

            return JsonResult.success("未绑定").set("hasBind", "0").set("obj", wl);
        }
    }

    /**
     * 生成二维码
     * @param params
     * @return
     */
    public JsonResult buildQr(String params) {
        String random = "lo_"+RandomTools.genTimeNo(5,5); //lo_开头表示是登陆用
        WxLogin wl = new WxLogin();
        wl.setCreateLong(System.currentTimeMillis()/1000); //秒
        wl.setToken(random);

        String qrStr = eventTools.getTempQr(random);
        if(qrStr!=null && !"".equals(qrStr)) {
            wl.setTicket(JsonTools.getJsonParam(qrStr, "ticket"));
        }

        wxLoginDao.save(wl); //保存

        return JsonResult.succ(wl);
    }

    /**
     * 通过Token获取对象
     * @param params
     * @return
     */
    public JsonResult loadByToken(String params) {
        String token = JsonTools.getJsonParam(params, "token");
        WxLogin wl = wxLoginDao.findByToken(token);
        if(wl==null) {
            return JsonResult.success("二维码过期，请刷新后重试").set("flag", "0");
        }
        if(wl.getOpenid()==null || "".equals(wl.getOpenid())) {
            return JsonResult.success("请打开微信扫一扫").set("flag", "0");
        }
        //TODO 根据openid获取用户权限，并将用户信息set到下方Result中
        return JsonResult.success("登陆成功").set("flag", "1");
    }

    /**
     * 微信扫码登陆验证
     * @param params {token:'abc'}
     * @return
     */
    public JsonResult wxLoginCheck(String params) {
        try {
            String token = JsonTools.getJsonParam(params, "token");
            WxLogin wl = wxLoginDao.findByToken(token);
            if(wl==null) {
                return JsonResult.success("二维码已过期，请刷新重试").set("error", "1");
            } else if(wl.getOpenid()==null || "".equals(wl.getOpenid())) {
                return JsonResult.success("请打开微信扫一扫").set("error", "1");
            } else {
                Relation r = relationDao.findByOpenid(wl.getOpenid());
                if(r==null || r.getUsername()==null || "".equals(r.getUsername())) {return JsonResult.success("该微信号没有绑定用户").set("error", "1");}
                OtherLogin login = otherLoginTools.onLogin(r.getUsername()); //生成Token
                return JsonResult.success("身份验证成功，正在登陆").set("username", r.getUsername()).set("token", login.getToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 微信扫码绑定验证
     * @param params {token:'abc',username:'root'}
     * @return
     */
    public JsonResult wxBindCheck(String params) {
        try {
            String token = JsonTools.getJsonParam(params, "token");
            String username = JsonTools.getJsonParam(params, "username");
            WxLogin wl = wxLoginDao.findByToken(token);
            if(wl==null) {
                return JsonResult.success("二维码已过期，请刷新重试").set("error", "1");
            } else if(wl.getOpenid()==null || "".equals(wl.getOpenid())) {
                return JsonResult.success("请打开微信扫一扫").set("error", "1");
            } else {
                Relation r = relationDao.findByOpenid(wl.getOpenid());
                Relation ur = relationDao.findByUsername(username);
                if(r==null && ur==null) {
                    r = new Relation();
                    r.setOpenid(wl.getOpenid());
                    r.setUsername(username);
                    relationDao.save(r);
                } else if(r!=null && ur==null) {
                    r.setUsername(username);
                    relationDao.save(r);
                } else if(r==null && ur!=null) {
                    ur.setOpenid(wl.getOpenid());
                    relationDao.save(ur);
                }
                return JsonResult.success("绑定成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
