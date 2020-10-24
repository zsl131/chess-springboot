package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.basic.dao.IVideoCategoryDao;
import com.zslin.bus.basic.iservice.IActivityService;
import com.zslin.bus.basic.model.VideoCategory;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Created by zsl on 2018/7/12.
 */
@Service
@AdminAuth(name = "视频分类", psn = "视频资源管理", url = "/admin/videoCategory", type = "1", orderNum = 3)
public class VideoCategoryService implements IActivityService {

    @Autowired
    private IVideoCategoryDao videoCategoryDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<VideoCategory> res = videoCategoryDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult listNoPage(String params) {
        List<VideoCategory> list = videoCategoryDao.findAll();
        return JsonResult.success().set("list", list);
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        VideoCategory cate = videoCategoryDao.findOne(id);
        return JsonResult.succ(cate);
    }

    public JsonResult addOrUpdate(String params) {
        VideoCategory obj = JSONObject.toJavaObject(JSON.parseObject(params), VideoCategory.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            VideoCategory cate = videoCategoryDao.getOne(obj.getId());
            if(cate.getPicPath()!=null && obj.getPicPath()!=null && !cate.getPicPath().equals(obj.getPicPath())) {
                //删除原来的图片
                File file = new File(configTools.getUploadPath() + cate.getPicPath());
                if(file.exists()) {file.delete();}
                cate.setPicPath(obj.getPicPath());
            }
            cate.setName(obj.getName());
            cate.setOrderNo(obj.getOrderNo());
            cate.setRemark(obj.getRemark());
            cate.setStatus(obj.getStatus());
            videoCategoryDao.save(cate);
        } else {
            videoCategoryDao.save(obj);
        }
        return JsonResult.success("保存成功");
    }

    public JsonResult delete(String params) {
        Integer id = JsonTools.getId(params);
        videoCategoryDao.delete(id);
        return JsonResult.success("删除成功");
    }
}
