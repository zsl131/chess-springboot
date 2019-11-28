package com.zslin.bus.qiniu.tools;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zslin.bus.qiniu.dto.MyPutRet;
import com.zslin.bus.qiniu.model.QiniuConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 上传工具类
 * Created by zsl on 2018/12/1.
 */
@Component
public class QiniuUploadTools {

    @Autowired
    private QiniuConfigTools qiniuConfigTools;

    /**
     * 上传文件到七牛
     * @param is 文件数据流
     * @param key 文件名
     * @return
     */
    public MyPutRet upload(InputStream is, String key) {
        //构造一个带指定Zone对象的配置类
        //华东	Zone.zone0()
        //华北	Zone.zone1()
        //华南	Zone.zone2()
        //北美	Zone.zoneNa0()
        //东南亚	Zone.zoneAs0()
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        QiniuConfig config = qiniuConfigTools.getQiniuConfig();

        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize),\"mimeType\":\"$(mimeType)\",\"ext\":\"$(ext)\", \"timeLen\":\"$(avinfo.format.duration)\"}");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(config.getBucketName(), null, expireSeconds, putPolicy);
//        System.out.println(upToken);

        try {
            Response response = uploadManager.put(is,key,upToken,null, null);
            //解析上传成功的结果
//            System.out.println(response.bodyString());
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //MyPutRet{key='15925061256', hash='lpOq47ndEyS76XDsecXnstPjdP9l', bucket='zhugan-job', fsize=55327966}
            //key='15925061256.mp4', hash='lhdGy3LAw9tQjktKx1z3fRer0dJC', bucket='zhugan-job', fsize=11541058, avinfo='null', mimeType='video/mp4', ext='.mp4'
            MyPutRet myPutRet=response.jsonToObject(MyPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//            System.out.println(myPutRet);
            return myPutRet;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return null;
    }
}
