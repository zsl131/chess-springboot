package com.zslin.bus.wx.service;

import com.alibaba.fastjson.JSON;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.model.WxAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/30.
 */
@Service
public class TempAccountService {

    @Autowired
    private IWxAccountDao wxAccountDao;

    public JsonResult queryAccount(String params) {
        String from = JsonTools.getJsonParam(params, "from");
        String openid = "0".equals(from)?"o4Jhl0nkSmiRIqU9JkWIm2lj6qXE":"orLIDuFuyeOygL0FBIuEilwCF1lU";
        WxAccount account = wxAccountDao.findByOpenid(openid);
        String str = JSON.toJSONString(account);
        System.out.println("str::"+str);
        return JsonResult.success(str);
    }
}
