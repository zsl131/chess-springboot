package com.zslin.bus.basic.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.dao.IActivityRecordImageDao;
import com.zslin.bus.basic.model.ActivityRecordImage;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ActivityRecordImageService {

    @Autowired
    private IActivityRecordImageDao activityRecordImageDao;

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ActivityRecordImage> res = activityRecordImageDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), JsonParamTools.buildAuthDepSpe(params)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        JsonResult result = JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
        return result;
    }

    /** 获取活动记录对应的照片信息 */
    public JsonResult listByRecordId(String params) {
        Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId"));
        List<ActivityRecordImage> list = activityRecordImageDao.findByRecordId(recordId, SimpleSortBuilder.generateSort("id_d"));
        return JsonResult.success().set("imageList", list);
    }

    public JsonResult deleteImage(String params) {
        Integer id = JsonTools.getId(params);
        ActivityRecordImage ari = activityRecordImageDao.findOne(id);
        File f = new File(configTools.getUploadPath() + ari.getImgUrl());
        if(f!=null) {f.delete();}
        activityRecordImageDao.delete(id);
        activityRecordDao.plusImgCount(-1, ari.getRecordId()); //删除后需要更新数量
        return JsonResult.success("删除成功");
    }
}
