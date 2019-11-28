package com.zslin.bus.wx.service;

import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.tools.JSApiTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/11/22.
 */
@Service
public class JSApiService {

    @Autowired
    private JSApiTools jsApiTools;

    /** 获取jsapi */
    public JsonResult buildJsApi(String params) {
        String url = JsonTools.getJsonParam(params, "url");
        return jsApiTools.buildJSTicket(url);
    }
}
