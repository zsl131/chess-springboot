package com.zslin.bus.share.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.share.dao.IShareUserDao;
import com.zslin.bus.share.model.ShareUser;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AdminAuth(name = "推广用户管理", psn = "推广管理", url = "/admin/shareUser", type = "1", orderNum = 1)
public class ShareUserService {

    @Autowired
    private IShareUserDao shareUserDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ShareUser> res = shareUserDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        ShareUser user = shareUserDao.findOne(id);
        return JsonResult.succ(user);
    }

    public JsonResult addOrUpdate(String params) {
        ShareUser obj = JSONObject.toJavaObject(JSON.parseObject(params), ShareUser.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            ShareUser user = shareUserDao.getOne(obj.getId());
            user.setName(obj.getName());
            user.setPhone(obj.getPhone());
            shareUserDao.save(user);
        } else {
            obj.setCreateDate(NormalTools.curDate());
            obj.setCreateTime(NormalTools.curDatetime());
            obj.setCreateLong(System.currentTimeMillis());
            shareUserDao.save(obj);
        }
        return JsonResult.success("保存成功");
    }

    public JsonResult delete(String params) {
        Integer id = JsonTools.getId(params);
        shareUserDao.delete(id);
        return JsonResult.success("删除成功");
    }
}
