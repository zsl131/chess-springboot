package com.zslin.bus.app.service;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.bus.app.dao.*;
import com.zslin.bus.app.model.App;
import com.zslin.bus.app.model.AppCourseComment;
import com.zslin.bus.app.model.AppSwiper;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IClassTagDao;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2019/8/20.
 */
@Service
public class AppService {

    @Autowired
    private IAppDao appDao;

    @Autowired
    private IAppSwiperDao appSwiperDao;

    @Autowired
    private IAppRemarkDao appRemarkDao;

    @Autowired
    private IClassTagDao classTagDao;

    @Autowired
    private IAppCourseCommentDao appCourseCommentDao;

    public JsonResult loadApp(String params) {
        App app = appDao.loadOne();
        return JsonResult.success("获取成功").set("app", app);
    }

    /**
     * 首页显示内容
     * @param params
     * @return
     */
    public JsonResult index(String params) {
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("status", "eq", "1");
        Page<AppSwiper> list = appSwiperDao.findAll(ssb.generate(), SimplePageBuilder.generate(0, SimpleSortBuilder.generateSort("orderNo_a")));
        Page<AppCourseComment> commentList = appCourseCommentDao.findAll(ssb.generate(), SimplePageBuilder.generate(0, 5, SimpleSortBuilder.generateSort("id_d")));
        JsonResult result = JsonResult.getInstance();
        List<ClassTag> tagList = classTagDao.findAll();
        result.set("swiperList", list.getContent()).set("remark", appRemarkDao.loadOne())
                .set("tagList", tagList).set("commentList", commentList.getContent());
        return result;
    }
}
