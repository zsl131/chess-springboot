package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.INoticeCategoryDao;
import com.zslin.bus.basic.dao.INoticeCommentDao;
import com.zslin.bus.basic.dao.INoticeDao;
import com.zslin.bus.basic.model.Notice;
import com.zslin.bus.basic.model.NoticeComment;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IAttachmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by zsl on 2018/9/15.
 */
@Service
@AdminAuth(name = "通知公告管理", psn = "系统管理", type = "1", url = "/admin/notice", orderNum = 10)
public class NoticeService {

    @Autowired
    private INoticeDao noticeDao;

    @Autowired
    private INoticeCategoryDao noticeCategoryDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private INoticeCommentDao noticeCommentDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Notice> res = noticeDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        JsonResult result =  JsonResult.getInstance().set("size", (int) res.getTotalElements())
                .set("data", res.getContent())
                .set("totalPage", res.getTotalPages());

        Integer cateId = null;
        try { cateId = Integer.parseInt(JsonTools.getJsonParam(params, "_cateId")); } catch (Exception e) { }
        if(cateId!=null && cateId>0) {
            result.set("category", noticeCategoryDao.findOne(cateId));
        }

        return result;
    }

    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            String isUpdate = JsonTools.getJsonParam(params, "isUpdate");
            Notice obj = noticeDao.findOne(id);
            JsonResult res = JsonResult.succ(obj);
            if(isUpdate!=null && "1".equals(isUpdate)) {res.set("cateList", noticeCategoryDao.findAll());}
            String hasAttachment = JsonTools.getJsonParam(params, "hasAttachment"); //是否有附件
            if(hasAttachment!=null && "1".equals(hasAttachment) && obj.getVideoId()!=null && obj.getVideoId()>0) {res.set("attachment", attachmentDao.findOne(obj.getVideoId()));}
            String findComment = JsonTools.getJsonParam(params, "findComment"); //是否获取评论
            if(findComment!=null && "1".equals(findComment)) {
                String openid = JsonTools.getJsonParam(params, "openid");
                QueryListDto qld = QueryTools.buildQueryListDto(params);
                Page<NoticeComment> comments = noticeCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                        new SpecificationOperator("noticeId", "eq", id) //活动详情Id
                        , new SpecificationOperator("status", "eq", "1",  new SpecificationOperator("openid", "eq", openid, "or")) //状态为显示
//                    , new SpecificationOperator("openid", "eq", openid, "or") //或openid为当前用户， 暂时去掉此功能
                        ),
                        SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
                res.set("commentSize", comments.getTotalElements()).set("commentList", comments.getContent()).set("totalPage", comments.getTotalPages());
            }
            return res;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult addOrUpdate(String params) {
        try {
            Notice obj = JSONObject.toJavaObject(JSON.parseObject(params), Notice.class);
            if(obj.getId()==null || obj.getId()<=0) {
                obj.setCreateTime(NormalTools.curDatetime());
                obj.setCreateLong(System.currentTimeMillis());
                obj.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
                noticeDao.save(obj); //添加
            } else {
                Notice d = noticeDao.findOne(obj.getId());
                if(d.getPicPath()!=null && !d.getPicPath().equals(obj.getPicPath())) {
                    //删除原来的图片
                    File file = new File(configTools.getUploadPath() + d.getPicPath());
                    if(file.exists()) {file.delete();}
                }
                MyBeanUtils.copyProperties(obj, d, "id");
                noticeDao.save(d);
            }
            return JsonResult.succ(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
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
            noticeDao.updateByHql("UPDATE Notice n SET n."+field+"=?1 WHERE n.id="+id, value);
            return JsonResult.success("设置成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            noticeDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
