package com.zslin.bus.share.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.bus.share.dao.IShareConfigDao;
import com.zslin.bus.share.model.ShareConfig;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AdminAuth(name = "推广配置管理", psn = "推广管理", url = "/admin/shareConfig", type = "1", orderNum = 3)
public class ShareConfigService {

    @Autowired
    private IShareConfigDao shareConfigDao;

    @Autowired
    private ConfigTools configTools;

    public JsonResult loadOne(String params) {
        ShareConfig sc = shareConfigDao.loadOne();
        if(sc==null) {sc = new ShareConfig();}
        return JsonResult.succ(sc);
    }

    public JsonResult save(String params) {
        ShareConfig sc = JSONObject.toJavaObject(JSON.parseObject(params), ShareConfig.class);
        ShareConfig scOld = shareConfigDao.loadOne();
        if(scOld==null) {
            /*sc.setQrWidth(Math.abs(sc.getEndX() - sc.getStartX()));
            sc.setQrHeight(Math.abs(sc.getEndY() - sc.getStartY()));*/
            sc.setEndX(sc.getStartX() + sc.getQrWidth());
            sc.setEndY(sc.getStartY() + sc.getQrHeight());
            shareConfigDao.save(sc);
            return JsonResult.succ(sc);
        } else {
            if(sc.getImgPath()!=null && scOld.getImgPath()!=null && !scOld.getImgPath().equals(sc.getImgPath())) {
                //删除原来的图片
                File file = new File(configTools.getUploadPath() + scOld.getImgPath());
                if(file.exists()) {file.delete();}
                scOld.setImgPath(sc.getImgPath());
            }
            MyBeanUtils.copyProperties(sc, scOld, "imgPath");

            /*scOld.setQrWidth(Math.abs(scOld.getEndX() - scOld.getStartX()));
            scOld.setQrHeight(Math.abs(scOld.getEndY() - scOld.getStartY()));*/
            scOld.setEndX(scOld.getStartX() + scOld.getQrWidth());
            scOld.setEndY(scOld.getStartY() + scOld.getQrHeight());
            shareConfigDao.save(scOld);
            return JsonResult.succ(scOld);
        }
    }
}
