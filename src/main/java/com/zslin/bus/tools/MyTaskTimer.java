package com.zslin.bus.tools;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.app.dao.IAppFeedbackImgDao;
import com.zslin.bus.app.model.AppFeedbackImg;
import com.zslin.bus.wx.dao.IWxLoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Created by zsl on 2018/9/25.
 */
@Component
public class MyTaskTimer {

    @Autowired
    private IWxLoginDao wxLoginDao;

    @Autowired
    private IAppFeedbackImgDao appFeedbackImgDao;

    @Autowired
    private ConfigTools configTools;

    /** 每天晚上11点半执行 */
    @Scheduled(cron="0 30 23 * * ?")
    public void deleteFeedbackImg() {
        long curLong = System.currentTimeMillis();
        List<AppFeedbackImg> imgList = appFeedbackImgDao.findByFlagAndCreateLong("0", curLong-(3600*3*1000));
        for(AppFeedbackImg img : imgList) {
            File f = new File(configTools.getUploadPath() + img.getUrl());
            if(f.exists()) {f.delete();}
            appFeedbackImgDao.delete(img);
        }
    }

    //启动30秒后执行第一次，然后每两小时执行一次
    @Scheduled(initialDelay = 30000, fixedDelay = 7200000)
    public void deleteWxLogin() {
//        List<WxLogin> list = wxLoginDao.findAll();
        Long curLong = System.currentTimeMillis()/1000;
        wxLoginDao.deleteByCreateLong(curLong-1800); //删除半小时以前的数据
    }
}
