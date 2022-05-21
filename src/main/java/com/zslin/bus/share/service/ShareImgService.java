package com.zslin.bus.share.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.share.dao.IShareImgDao;
import com.zslin.bus.share.model.ShareImg;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AdminAuth(name = "推广图片管理", psn = "推广管理", url = "/admin/shareImg", type = "1", orderNum = 3)
public class ShareImgService {

    @Autowired
    private IShareImgDao shareImgDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ShareImg> res = shareImgDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.success().set("size", (int)res.getTotalElements()).set("data", res.getContent());
    }

    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        ShareImg img = shareImgDao.findOne(id);
        return JsonResult.succ(img);
    }

    public JsonResult addOrUpdate(String params) {
        ShareImg obj = JSONObject.toJavaObject(JSON.parseObject(params), ShareImg.class);
        if(obj.getId()!=null && obj.getId()>0) { //修改
            ShareImg img = shareImgDao.getOne(obj.getId());
            if(obj.getImgPath()!=null && obj.getImgPath()!=null && !obj.getImgPath().equals(img.getImgPath())) {
                //删除原来的图片
                File file = new File(configTools.getUploadPath() + img.getImgPath());
                if(file.exists()) {file.delete();}
                img.setImgPath(obj.getImgPath());
            }
            MyBeanUtils.copyProperties(obj, img, "imgPath");

            img.setEndX(obj.getStartX() + obj.getQrWidth());
            img.setEndY(obj.getStartY() + obj.getQrHeight());
            shareImgDao.save(img);
//            return JsonResult.succ(img);
        } else {

            obj.setEndX(obj.getStartX() + obj.getQrWidth());
            obj.setEndY(obj.getStartY() + obj.getQrHeight());
            shareImgDao.save(obj);
        }
        return JsonResult.success("保存成功");
    }

    public JsonResult delete(String params) {
        Integer id = JsonTools.getId(params);
        ShareImg img = shareImgDao.findOne(id);
        if(img.getImgPath()!=null) {
            File file = new File(configTools.getUploadPath() + img.getImgPath());
            if(file.exists()) {file.delete();}
        }
        shareImgDao.delete(id);
        return JsonResult.success("删除成功");
    }
}
