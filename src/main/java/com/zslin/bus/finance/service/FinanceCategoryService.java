package com.zslin.bus.finance.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.finance.dao.IFinanceCategoryDao;
import com.zslin.bus.finance.model.FinanceCategory;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2019/1/3.
 */
@Service
@AdminAuth(name = "账务分类", psn = "财务管理", url = "/admin/financeCategory", type = "1", orderNum = 1)
public class FinanceCategoryService {

    @Autowired
    private IFinanceCategoryDao financeCategoryDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<FinanceCategory> res = financeCategoryDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult listNoPage(String params) {
        List<FinanceCategory> list = financeCategoryDao.findAll();
        return JsonResult.success().set("list", list);
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        FinanceCategory cate = financeCategoryDao.findOne(id);
        return JsonResult.succ(cate);
    }

    public JsonResult addOrUpdate(String params) {
        FinanceCategory obj = JSONObject.toJavaObject(JSON.parseObject(params), FinanceCategory.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            FinanceCategory cate = financeCategoryDao.getOne(obj.getId());
            cate.setName(obj.getName());
            financeCategoryDao.save(cate);
        } else {
            financeCategoryDao.save(obj);
        }
        return JsonResult.success("保存成功");
    }

    public JsonResult delete(String params) {
        Integer id = JsonTools.getId(params);
        financeCategoryDao.delete(id);
        return JsonResult.success("删除成功");
    }
}
