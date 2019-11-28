package com.zslin.bus.finance.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.dao.IUserDao;
import com.zslin.basic.model.User;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.finance.dao.IFinanceCategoryDao;
import com.zslin.bus.finance.dao.IFinanceDetailDao;
import com.zslin.bus.finance.model.FinanceDetail;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2019/1/2.
 */
@Service
@AdminAuth(name = "账务流水", psn = "财务管理", url = "/admin/financeDetail", type = "1", orderNum = 1)
public class FinanceDetailService {

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    @Autowired
    private IUserDao userDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<FinanceDetail> res = financeDetailDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        Float totalIn = financeDetailDao.sum("1");
        Float totalOut = financeDetailDao.sum("-1");
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent())
                .set("totalIn", totalIn).set("totalOut", totalOut).set("cateList", financeCategoryDao.findAll());
    }

    public JsonResult loadOne(String params) {
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        FinanceDetail fd = financeDetailDao.findOne(id);
        return JsonResult.succ(fd);
    }

    public JsonResult save(String params) {
        FinanceDetail obj = JSONObject.toJavaObject(JSON.parseObject(params), FinanceDetail.class);
        obj.setCreateDate(NormalTools.curDate());
        obj.setCreateTime(NormalTools.curDatetime());
        obj.setCreateLong(System.currentTimeMillis());
        obj.setCateName(financeCategoryDao.findOne(obj.getCateId()).getName());
        try {
            String username = getUsername(params);
            User u = userDao.findByUsername(username);
            obj.setRecordName(username+"-"+u.getNickname());
            obj.setRecordPhone(u.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String recordDate = obj.getRecordDate().replaceAll("-", "").substring(0, 8); //20190102
        String recordYear = recordDate.substring(0, 4);
        String recordMonth = recordDate.substring(0, 6);
        obj.setRecordDate(recordDate);
        obj.setRecordMonth(recordMonth);
        obj.setRecordYear(recordYear);
        obj.setStatus("1");
        FinanceDetail fd = financeDetailDao.findOne(obj.getId());
        MyBeanUtils.copyProperties(obj, fd, true);
        //设置用户
        financeDetailDao.save(fd);
        return JsonResult.success("保存成功");
    }

    public JsonResult updateStatus(String params) {
        String status = JsonTools.getJsonParam(params, "status");
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
        String reason = JsonTools.getJsonParam(params, "reason");
        String name = "", phone = "";
        try {
            String username = getUsername(params);
            User u = userDao.findByUsername(username);
            name = u.getUsername()+"-"+u.getNickname();
            phone = u.getPhone();
        } catch (Exception e) {
        }
        financeDetailDao.updateStatus(status, reason, name, phone, id);
        return JsonResult.success("操作成功");
    }

    private String getUsername(String params) {
        try {
            String headerParams = JsonTools.getJsonParam(params, "headerParams");
            String username = JsonTools.getJsonParam(headerParams, "username");
            return username;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
