package com.zslin.bus.common.controller;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.dao.IActivityRecordImageDao;
import com.zslin.bus.basic.model.ActivityRecordImage;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zsl on 2018/7/12.
 */
@RestController
@RequestMapping(value = "api/upload")
public class UploadRecordImageController {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IActivityRecordImageDao activityRecordImageDao;

    @Autowired
    private IActivityRecordDao activityRecordDao;

    private static final String UPLOAD_PATH_PRE = "/publicFile/upload";

    /**
     * 保存文件，直接以multipartFile形式
     * @param multipartFile
     * @return 返回文件名
     * @throws IOException
     */
    @RequestMapping(value = "recordImage")
    public List<ActivityRecordImage> uploadFile(@RequestParam("file")MultipartFile[] multipartFile, Integer recordId, Integer actId, String actTitle, String holdTime, String address) throws IOException {
//        System.out.println("======="+path+"=====length:"+multipartFile.length);
//        System.out.println("recordId::"+recordId+"=========actID::"+actId+"=======title: "+actTitle);
        List<ActivityRecordImage> result = new ArrayList<>();
        Integer count = 0;
        if(multipartFile!=null && multipartFile.length>=1) {
            for(int i=0;i<multipartFile.length;i++) {
//                MultipartFile mf = multipartFile[i];
//                System.out.println(mf.getName() + "===" + mf.getOriginalFilename() + "===" + mf.getContentType());
//                FileInputStream fileInputStream = (FileInputStream) mf.getInputStream();

                MultipartFile file = multipartFile[i];
                String fileName = file.getOriginalFilename();
//                    System.out.println("========fileName::"+fileName);
                if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                    File outFile = new File(configTools.getUploadPath(UPLOAD_PATH_PRE) + File.separator + NormalTools.curDate("yyyyMMdd") + File.separator + UUID.randomUUID().toString() + NormalTools.getFileType(fileName));
                    String imageUrl = outFile.getAbsolutePath().replace(configTools.getUploadPath(), File.separator).replaceAll("\\\\", "/");
                    FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
                    try {
                        Thumbnails.of(outFile).size(500, 500).toFile(outFile);
                    } catch (Exception e) {
                    }
//                result.add(uploadPath);
                    ActivityRecordImage ari = new ActivityRecordImage();
                    ari.setActId(actId);
                    ari.setActTitle(actTitle);
                    ari.setImgUrl(imageUrl);
                    ari.setRecordId(recordId);
                    ari.setTitle(actTitle);
                    ari.setRecordAddress(address);
                    ari.setRecordHoldTime(holdTime);
                    activityRecordImageDao.save(ari);
                    result.add(ari);
                    count++;
                }
            }
        }
        if(count>0) {
            activityRecordDao.plusImgCount(count, recordId);
        }
        return result;
    }
}