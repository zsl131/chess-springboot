package com.zslin.bus.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxMenuDao;
import com.zslin.bus.wx.dto.WxMenuDto;
import com.zslin.bus.wx.dto.WxMenuTreeDto;
import com.zslin.bus.wx.model.WxMenu;
import com.zslin.bus.wx.tools.AccessTokenTools;
import com.zslin.bus.wx.tools.WeixinUtil;
import com.zslin.bus.wx.tools.WxConfigTools;
import com.zslin.bus.wx.tools.WxMenuTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zsl on 2018/7/20.
 */
@Service
@AdminAuth(name = "微信菜单管理", psn = "微信管理", orderNum = 2, url = "admin/wxMenu", type = "1")
public class WxMenuService {

    @Autowired
    private IWxMenuDao wxMenuDao;

    @Autowired
    private WxMenuTools wxMenuTools;

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private WxConfigTools wxConfigTools;

    public JsonResult listRoot(String params) {
        Integer pid = 0;
        try { pid = Integer.parseInt(JsonTools.getJsonParam(params, "pid"));} catch (Exception e) {pid=0;}
        List<WxMenuDto> list = wxMenuTools.buildMenuTree();
        Sort sort = SimpleSortBuilder.generateSort("orderNo_a");
        List<WxMenu> menuList ;
        if(pid==0) {
            menuList = wxMenuDao.findRootMenu(sort);
        } else {
            menuList = wxMenuDao.findByParent(pid, sort);
        }
        WxMenuTreeDto trd = new WxMenuTreeDto(list, menuList);
        return JsonResult.getInstance().set("size", menuList.size()).set("datas", trd);
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
        List<WxMenu> menuList ;
        if(pid==0) {
            menuList = wxMenuDao.findRootMenu(sort);
        } else {
            menuList = wxMenuDao.findByParent(pid, sort);
        }

        return JsonResult.getInstance().set("size", menuList.size()).set("datas", menuList);
    }

    public JsonResult add(String params) {
        try {
            WxMenu menu = JSONObject.toJavaObject(JSON.parseObject(params), WxMenu.class);
            wxMenuDao.save(menu);
            return JsonResult.succ(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult update(String params) {
        try {
            WxMenu menu = JSONObject.toJavaObject(JSON.parseObject(params), WxMenu.class);
            WxMenu m = wxMenuDao.findOne(menu.getId());
            m.setOrderNo(menu.getOrderNo());
            m.setUrl(menu.getUrl());
            m.setName(menu.getName());
            wxMenuDao.save(m);
            return JsonResult.succ(m);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            wxMenuDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult synchMenu(String params) {
        try {
            String json = createMenuJson();
            System.out.println("---wxMenu::"+json);
            String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ accessTokenTools.getAccessToken();
            org.json.JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", json);
            System.out.println("====createMenu result::"+jsonObj.toString());
            String code = JsonTools.getJsonParam(jsonObj.toString(), "errcode");
//            System.out.println("=====WxMenuService.synchMenu:::"+code);
            if("0".equals(code)) {
                return JsonResult.success("发布菜单成功");
            } else {
                return JsonResult.error("发布菜单失败");
            }
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }
    }

    private String createMenuJson() {
        StringBuffer sb = new StringBuffer("{\"button\":[");
        List<WxMenu> parents = wxMenuDao.findRootMenu(SimpleSortBuilder.generateSort("orderNo"));
        int temp = 0;
        for(WxMenu p : parents) {
            sb.append(createSinglMenuJson(p));
            temp++;
            if(temp<parents.size()) {sb.append(",");}
        }
        sb.append("]}");
        return sb.toString();
    }

    private String createSinglMenuJson(WxMenu menu) {
        StringBuffer sb = new StringBuffer("{");
        List<WxMenu> suns = wxMenuDao.findByParent(menu.getId(), SimpleSortBuilder.generateSort("orderNo"));
        sb.append("\"name\":\"").append(menu.getName()).append("\"");
        if(suns==null || suns.size()<=0) {
            //无子菜单
            sb.append(createMenu(menu));
        } else {
            sb.append(",\"sub_button\":[");
            int temp = 0;
            for(WxMenu sun : suns) {
                sb.append("{");
                sb.append("\"name\":\"").append(sun.getName()).append("\"");
                sb.append(createMenu(sun));
                sb.append("}");
                temp ++;
                if(temp<suns.size()) {sb.append(",");}
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    private String createMenu(WxMenu menu) {
        String url = menu.getUrl();
        if(!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) { //如果不是http链接
            if(!url.startsWith("/")) {url = "/"+url;}
            url = wxConfigTools.getWxConfig().getUrl()+url;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(",\"type\":\"view\"");
        sb.append(",\"url\":\"").append(url).append("\"");
        return sb.toString();
    }
}
