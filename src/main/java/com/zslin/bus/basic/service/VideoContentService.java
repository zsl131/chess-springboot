package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IVideoCategoryDao;
import com.zslin.bus.basic.dao.IVideoContentDao;
import com.zslin.bus.basic.model.VideoCategory;
import com.zslin.bus.basic.model.VideoContent;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AdminAuth(name = "视频内容管理", psn = "视频资源管理", type = "1", url = "/admin/videoContent", orderNum = 10)
public class VideoContentService {

    @Autowired
    private IVideoCategoryDao videoCategoryDao;

    @Autowired
    private IVideoContentDao videoContentDao;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<VideoContent> res = videoContentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        JsonResult result =  JsonResult.getInstance().set("size", (int) res.getTotalElements())
                .set("data", res.getContent())
                .set("totalPage", res.getTotalPages());

        Integer cateId = null;
        try { cateId = Integer.parseInt(JsonTools.getJsonParam(params, "_cateId")); } catch (Exception e) { }
        if(cateId!=null && cateId>0) {
            result.set("category", videoCategoryDao.findOne(cateId));
        }

        List<VideoCategory> categoryList = videoCategoryDao.findByShow(SimpleSortBuilder.generateSort("orderNo"));
        result.set("categoryList", categoryList);

        return result;
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            VideoContent obj = videoContentDao.findOne(id);
            List<VideoCategory> categoryList = videoCategoryDao.findByShow(SimpleSortBuilder.generateSort("orderNo"));
//            result.set("categoryList", categoryList);
            JsonResult res = JsonResult.succ(obj).set("categoryList", categoryList);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        try {
            VideoContent obj = JSONObject.toJavaObject(JSON.parseObject(params), VideoContent.class);
            if(obj.getId()==null || obj.getId()<=0) { //添加
                obj.setCreateTime(NormalTools.curDatetime());
                obj.setCreateLong(System.currentTimeMillis());
                obj.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));

                if(obj.getShowDate()!=null && !"".equals(obj.getShowDate())) {
                    obj.setShowDateLong(buildLong(obj.getShowDate()));
                } else {
                    obj.setShowDateLong(System.currentTimeMillis());
                    obj.setShowDate(obj.getCreateTime());
                }

                videoContentDao.save(obj); //添加
            } else { //修改
                VideoContent d = videoContentDao.findOne(obj.getId());
                MyBeanUtils.copyProperties(obj, d, "id");
                if(obj.getShowDate()!=null && !"".equals(obj.getShowDate())) {
                    d.setShowDate(obj.getShowDate());
                    d.setShowDateLong(buildLong(obj.getShowDate()));
                } else {
//                    d.setShowDate(NormalTools.curDate());
//                    d.setShowDateLong(System.currentTimeMillis());
                }
                videoContentDao.save(d);
            }
            return JsonResult.succ(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    private Long buildLong(String holdTime) {
        Long res = NormalTools.str2Long(holdTime, "yyyy-MM-dd");
        return res;
    }

    /**
     * 修改属性值
     * @param params {id: 0, field:'isTop', value: '0'}
     * @return
     */
    public JsonResult updateField(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String field = JsonTools.getJsonParam(params, "field");
            String value = JsonTools.getJsonParam(params, "value");
            videoContentDao.updateByHql("UPDATE VideoContent n SET n."+field+"=?1 WHERE n.id="+id, value);
            return JsonResult.success("设置成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            videoContentDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
