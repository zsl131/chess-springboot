package com.zslin.bus.common.controller;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppFeedbackImgDao;
import com.zslin.bus.app.model.AppFeedbackImg;
import com.zslin.bus.common.controller.dto.UploadResult;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zsl on 2019/9/24.
 */
@RestController
@RequestMapping(value = "api/app/upload")
public class AppUploadController {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IAppFeedbackImgDao appFeedbackImgDao;

    private static final String UPLOAD_PATH_PRE = "/publicFile/app";

    @RequestMapping(value = "feedback")
    public UploadResult feedback(@RequestParam("files") MultipartFile[] multipartFile, String randomId) {
        UploadResult result = new UploadResult(0);
        try {
            if(multipartFile!=null && multipartFile.length>=1) {
                MultipartFile mf = multipartFile[0];
                //                System.out.println(mf.getName() + "===" + mf.getOriginalFilename() + "===" + mf.getContentType());
                FileInputStream fileInputStream = (FileInputStream) mf.getInputStream();

                MultipartFile file = multipartFile[0];
                String fileName = file.getOriginalFilename();
                //                    System.out.println("========fileName::"+fileName);
                if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && NormalTools.isImageFile(fileName)) {
                    File outFile = new File(configTools.getUploadPath(UPLOAD_PATH_PRE) + File.separator + "feedback" + File.separator + NormalTools.curDate("yyyyMMdd") + File.separator + UUID.randomUUID().toString() + NormalTools.getFileType(fileName));
                    String uploadPath = outFile.getAbsolutePath().replace(configTools.getUploadPath(), File.separator);
                    uploadPath = uploadPath.replaceAll("\\\\", "/");
                    FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
                    AppFeedbackImg afi = new AppFeedbackImg();
                    afi.setUrl(uploadPath);
                    afi.setRandomId(randomId);
                    afi.setCreateLong(System.currentTimeMillis());
                    appFeedbackImgDao.save(afi);
                    Thumbnails.of(outFile).size(1000, 1000).toFile(outFile);
                    //                result.add(uploadPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
