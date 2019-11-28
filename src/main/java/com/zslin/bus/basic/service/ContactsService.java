package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.basic.dao.IContactsDao;
import com.zslin.bus.basic.model.Contacts;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AdminAuth(name = "通讯录管理", psn = "系统管理", url = "/admin/contacts", type = "1", orderNum = 9)
public class ContactsService {
    @Autowired

    private IContactsDao contactsDao;

    public JsonResult list(String params) {
        try {
            QueryListDto qld = QueryTools.buildQueryListDto(params);
            Page<Contacts> res = contactsDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                    SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
            return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        Contacts a = JSON.toJavaObject(JSON.parseObject(params),Contacts.class);
        if(a.getId()!=null && a.getId()>0) {
            Contacts s = contactsDao.findOne(a.getId());
            s.setName(a.getName());
            s.setSex(a.getSex());
            s.setDepName(a.getDepName());
            s.setDuty(a.getDuty());
            s.setPhone(a.getPhone());
            s.setRemark(a.getRemark());
            contactsDao.save(s);
            return JsonResult.success("修改成功");
        } else {
            contactsDao.save(a);
            return JsonResult.success("保存成功");
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
            contactsDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
            Contacts obj = contactsDao.findOne(id);
            return JsonResult.succ(obj);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
