package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.bus.basic.dao.INoticeDao;
import com.zslin.bus.basic.model.Notice;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IAttachmentDao;
import com.zslin.bus.yard.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class AppNoticeService {

    @Autowired
    private INoticeDao noticeDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    /**
     * 根据年级获取课程
     * @param params 必须包含年级ID
     * @return
     */
    public JsonResult listNotice(String params) {
        Integer cid = Integer.parseInt(JsonTools.getJsonParam(params, "cateId")); //NoticeCategory ID
        Integer page = 0;
        try {
            page = Integer.parseInt(JsonTools.getJsonParam(params, "page"));
        } catch (Exception e) {
            page = 0;
        }
        JsonResult result = JsonResult.success();
//        System.out.println("========gid::"+gid+", PHONE::"+phone);
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("cateId", "eq", cid);
        ssb.add(new SpecificationOperator("status", "eq", "1"));
        Page<Notice> datas = noticeDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        result.set("data", datas.getContent());
        result.set("cateId", cid);
        return result;
    }

    /**
     * 获取一个对象
     * @param params
     * @return
     */
    public JsonResult loadOne(String params) {
        Integer id = JsonTools.getId(params);
        Notice notice = noticeDao.findOne(id);
        Integer videoId = notice.getVideoId();
        Attachment atta = null;
        if(videoId!=null && videoId>0) {atta = attachmentDao.findOne(videoId);}
        return JsonResult.succ(notice).set("atta", atta);
    }
}
