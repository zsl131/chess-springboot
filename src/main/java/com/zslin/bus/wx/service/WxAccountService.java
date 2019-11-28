package com.zslin.bus.wx.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.model.WxAccount;
import com.zslin.bus.wx.tools.DatasTools;
import com.zslin.bus.wx.tools.ExchangeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/21.
 */
@Service
@AdminAuth(name = "微信用户", psn = "微信管理", orderNum = 3, type = "1", url = "/admin/wxAccount")
public class WxAccountService {

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private DatasTools datasTools;

    @Autowired
    private ExchangeTools exchangeTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<WxAccount> res = wxAccountDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", res.getTotalElements()).set("datas", res.getContent());
    }

    public JsonResult updateType(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String type = JsonTools.getJsonParam(params, "type");
            wxAccountDao.updateType(id, type);
            return JsonResult.success("设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 同步微信用户
     * @param params id:1
     * @return
     */
    public JsonResult updateAccount(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            datasTools.updateAccount(id);
            WxAccount a = wxAccountDao.findOne(id);
            return JsonResult.succ(a);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult queryAccountByCode(String params) {
        try {
            String code = JsonTools.getJsonParam(params, "code");
            String openid = exchangeTools.getUserOpenId(code);
            WxAccount account = wxAccountDao.findByOpenid(openid);
            return JsonResult.succ(account);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult queryAccountByOpenid(String params) {
        String openid = JsonTools.getJsonParam(params, "openid");
        WxAccount account = wxAccountDao.findByOpenid(openid);
        return JsonResult.succ(account);
    }

}
