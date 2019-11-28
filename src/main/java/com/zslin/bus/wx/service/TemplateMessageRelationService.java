package com.zslin.bus.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.ITemplateMessageRelationDao;
import com.zslin.bus.wx.dto.TemplateMessageDto;
import com.zslin.bus.wx.model.TemplateMessageRelation;
import com.zslin.bus.wx.tools.TemplateMessageAnnotationTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/8/28.
 */
@Service
@AdminAuth(name = "模板消息管理", psn = "微信管理", orderNum = 5, type = "1", url = "/admin/templateMessageRelation")
public class TemplateMessageRelationService {

    @Autowired
    private ITemplateMessageRelationDao templateMessageRelationDao;

    @Autowired
    private TemplateMessageAnnotationTools templateMessageAnnotationTools;

    public JsonResult noConfig(String params) {
        List<TemplateMessageDto> list = templateMessageAnnotationTools.findNoConfigTemplateMessage();
        return JsonResult.getInstance().set("list", list);
    }

    /** 全部已配置的数据 */
    public JsonResult list(String params) {
        List<TemplateMessageRelation> configed = templateMessageRelationDao.findAll();
        List<TemplateMessageDto> noConfig = templateMessageAnnotationTools.findNoConfigTemplateMessage();
        return JsonResult.getInstance().set("noConfig", noConfig).set("configed", configed);
    }

    public JsonResult add(String params) {
        try {
            TemplateMessageRelation tmr = JSONObject.toJavaObject(JSON.parseObject(params), TemplateMessageRelation.class);
            tmr.setTemplatePinyin(PinyinToolkit.cn2Spell(tmr.getTemplateName(), ""));
            templateMessageRelationDao.save(tmr);
            return JsonResult.success("配置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            templateMessageRelationDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (NumberFormatException e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
