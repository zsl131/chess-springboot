package com.zslin.bus.wx.controller;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.basic.dao.IVideoCategoryDao;
import com.zslin.bus.basic.dao.IVideoContentDao;
import com.zslin.bus.basic.model.VideoCategory;
import com.zslin.bus.basic.model.VideoContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 视频资源微信端
 */
@Controller
@RequestMapping(value = "weixin/video")
public class WeixinVideoController {

    @Autowired
    private IVideoCategoryDao videoCategoryDao;

    @Autowired
    private IVideoContentDao videoContentDao;

    @GetMapping(value = {"", "index", "/"})
    public String index(HttpServletRequest request, Model model) {
        List<VideoCategory> categoryList = videoCategoryDao.findByShow(SimpleSortBuilder.generateSort("orderNo_a"));
        model.addAttribute("categoryList", categoryList);
        return "weixin/video/index";
    }

    @GetMapping(value = "list")
    public String list(Model model, Integer cateId, String sort) {
        VideoCategory category = videoCategoryDao.findOne(cateId);
        sort = (sort==null||"".equals(sort))?"orderNo_a":sort;
        List<VideoContent> contentList = videoContentDao.listByCate(cateId, SimpleSortBuilder.generateSort(sort));
        model.addAttribute("category", category);
        model.addAttribute("contentList", contentList);
        return "weixin/video/list";
    }

    @GetMapping(value = "show")
    public String show(Model model, Integer id) {
        VideoContent vc = videoContentDao.findOne(id);
        videoContentDao.plusReadCount(1, id);
        model.addAttribute("videoContent", vc);
        return "weixin/video/show";
    }
}
