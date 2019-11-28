package com.zslin.bus.basic.service;

import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dto.TemplateMessageDto;
import com.zslin.bus.wx.tools.ScoreAnnotationTools;
import com.zslin.bus.wx.tools.TemplateMessageAnnotationTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2018/9/4.
 */
@Service
public class AdminIndexService {

    @Autowired
    private TemplateMessageAnnotationTools templateMessageAnnotationTools;

    @Autowired
    private ScoreAnnotationTools scoreAnnotationTools;

    public JsonResult noConfig(String params) {
        List<TemplateMessageDto> noConfigTemplateMessage = templateMessageAnnotationTools.findNoConfigTemplateMessage();
        List<String> noConfigScore = scoreAnnotationTools.findNoConfigScore();
        return JsonResult.getInstance().set("templateMessage", noConfigTemplateMessage).set("score", noConfigScore);
    }
}
