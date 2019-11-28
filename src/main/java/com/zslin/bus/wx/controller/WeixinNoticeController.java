package com.zslin.bus.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.bus.basic.dao.INoticeDao;
import com.zslin.bus.basic.model.Notice;
import com.zslin.bus.yard.dao.IAttachmentDao;
import com.zslin.bus.yard.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2019/6/23.
 */
@RequestMapping(value = "weixin/notice")
@Controller
public class WeixinNoticeController {

    @Autowired
    private INoticeDao noticeDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @GetMapping(value = "index")
    public String index(Model model, Integer page, HttpServletRequest request) {
        Page<Notice> datas = noticeDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request, new SpecificationOperator("status", "eq", "1")),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/notice/index";
    }

    @GetMapping(value = "show")
    public String show(Model model, Integer id, HttpServletRequest request) {
        Notice notice = noticeDao.findOne(id);
        if(notice.getVideoId()!=null && notice.getVideoId()>0) {
            Attachment atta = attachmentDao.findOne(notice.getVideoId());
            model.addAttribute("video", atta);
        } else {
            model.addAttribute("video", null);
        }
        model.addAttribute("notice", notice);
        return "weixin/notice/show";
    }
}
