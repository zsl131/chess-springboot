package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IDraftDao;
import com.zslin.bus.basic.model.Draft;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * 小视频文稿
 */
@Service
@AdminAuth(name = "视频文稿", psn = "任务专栏", url = "/admin/draft", type = "1", orderNum = 6)
public class DraftService {

    @Autowired
    private IDraftDao draftDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Draft> res = draftDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        return JsonResult.getInstance().set("size", (int) res.getTotalElements())
                .set("data", res.getContent())
                .set("totalPage", res.getTotalPages());
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Draft obj = draftDao.findOne(id);
            return JsonResult.succ(obj);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        try {
            Draft obj = JSONObject.toJavaObject(JSON.parseObject(params), Draft.class);
            if(obj.getId()==null || obj.getId()<=0) { //添加
                obj.setCreateTime(NormalTools.curDatetime());
                obj.setCreateLong(System.currentTimeMillis());
                obj.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));

                obj.setUpdateTime(NormalTools.curDatetime());
                obj.setUpdateLong(System.currentTimeMillis());
                obj.setUpdateDay(NormalTools.curDate("yyyy-MM-dd"));
                obj.setUpdateDate(Integer.parseInt(NormalTools.curDate("yyyyMMdd")));

                draftDao.save(obj); //添加
            } else {
                Draft d = draftDao.findOne(obj.getId());
                MyBeanUtils.copyProperties(obj, d, "id", "updateDay", "updateTime", "updateLong", "updateDate", "authorName", "authorPhone", "authorUsername", "authorId");
                d.setUpdateTime(NormalTools.curDatetime());
                d.setUpdateLong(System.currentTimeMillis());
                d.setUpdateDay(NormalTools.curDate("yyyy-MM-dd"));
                d.setUpdateDate(Integer.parseInt(NormalTools.curDate("yyyyMMdd")));
                draftDao.save(d);
            }
            return JsonResult.succ(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            draftDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
