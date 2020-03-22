package com.zslin.bus.common.controller;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.app.dao.IAppFeedbackImgDao;
import com.zslin.bus.app.model.AppFeedbackImg;
import com.zslin.bus.common.controller.dto.UploadResult;
import com.zslin.bus.qiniu.tools.QiniuConfigTools;
import com.zslin.bus.qiniu.tools.QiniuUploadTools;
import com.zslin.bus.yard.dao.IClassCommentDao;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassImageDao;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.ClassComment;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassImage;
import com.zslin.bus.yard.model.Teacher;
import com.zslin.bus.yard.tools.MyFileTools;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private QiniuUploadTools qiniuUploadTools;

    @Autowired
    private QiniuConfigTools qiniuConfigTools;

    @Autowired
    private IClassImageDao classImageDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private IClassCommentDao classCommentDao;

    @Autowired
    private ITeacherDao teacherDao;

    /**
     * 上传课堂照片
     * @param multipartFile
     * @param phone 教师手机号码
     * @param courseId 课程ID
     * @return
     */
    @RequestMapping(value = "classImage")
    public UploadResult classImage(@RequestParam("files") MultipartFile[] multipartFile, String phone, Integer courseId) {
        UploadResult result = new UploadResult(0);
        try {
            if(multipartFile!=null && multipartFile.length>=1) {
                MultipartFile mf = multipartFile[0];
                //                System.out.println(mf.getName() + "===" + mf.getOriginalFilename() + "===" + mf.getContentType());

                MultipartFile file = multipartFile[0];
                String fileName = file.getOriginalFilename();

                String fileType = MyFileTools.getFileType(fileName);

                String type = "";//1-图片；2-视频；
                if(MyFileTools.isImageFile(fileName)) {type = "1";}
                else if(MyFileTools.isVideoFile(fileName)) {type = "2"; }

                String key = UUID.randomUUID().toString()+fileType.toLowerCase();
                if("1".equals(type)) { //图片
                    File outFile = new File(configTools.getUploadPath(UPLOAD_PATH_PRE) + File.separator + "temp" + File.separator + UUID.randomUUID().toString() + fileType);
                    FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);

                    Thumbnails.of(outFile).size(600, 600).toFile(outFile);

                    qiniuUploadTools.upload(file.getInputStream(), key);
                    outFile.delete(); //传到七牛就删除本地
                } else if("2".equals(type)) { //是视频
                    //String key = System.currentTimeMillis() + fileType.toLowerCase();
                    qiniuUploadTools.upload(file.getInputStream(), key);
                }

                ClassImage ci = new ClassImage();
                ClassCourse course = classCourseDao.findOne(courseId);
                Teacher tea = teacherDao.findByPhone(phone);
                ci.setCourseId(courseId);
                ci.setCourseTitle(course.getTitle());
                ci.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
                ci.setCreateLong(System.currentTimeMillis());
                ci.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                ci.setCreateYear(NormalTools.curDate("yyyy"));
                ci.setFileType(type);
                ci.setSchId(tea.getSchoolId());
                ci.setSchName(tea.getSchoolName());
                ci.setStatus("0");
                ci.setTeaId(tea.getId());
                ci.setTeaName(tea.getName());
                ci.setTeaPhone(phone);
                ci.setUrl(qiniuConfigTools.getQiniuConfig().getUrl() + key);
                classImageDao.save(ci);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 上传课堂照片
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "classComment")
    public UploadResult classComment(@RequestParam("files") MultipartFile[] multipartFile, Integer teaId, String content) {
        UploadResult result = new UploadResult(0);
        try {
            if(multipartFile!=null && multipartFile.length>=1) {
                MultipartFile mf = multipartFile[0];
                //                System.out.println(mf.getName() + "===" + mf.getOriginalFilename() + "===" + mf.getContentType());

                MultipartFile file = multipartFile[0];
                String fileName = file.getOriginalFilename();

                String fileType = MyFileTools.getFileType(fileName);

                String type = "";//1-图片；2-视频；
                if(MyFileTools.isImageFile(fileName)) {type = "1";}
                else if(MyFileTools.isVideoFile(fileName)) {type = "2"; }

                String key = UUID.randomUUID().toString()+fileType.toLowerCase();
                if("1".equals(type)) { //图片
                    File outFile = new File(configTools.getUploadPath(UPLOAD_PATH_PRE) + File.separator + "temp" + File.separator + UUID.randomUUID().toString() + fileType);
                    FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);

                    Thumbnails.of(outFile).size(600, 600).toFile(outFile);

                    qiniuUploadTools.upload(file.getInputStream(), key);
                    outFile.delete(); //传到七牛就删除本地
                } else if("2".equals(type)) { //是视频
                    //String key = System.currentTimeMillis() + fileType.toLowerCase();
                    qiniuUploadTools.upload(file.getInputStream(), key);
                }

                ClassComment cc = new ClassComment();
                Teacher tea = teacherDao.findOne(teaId);
                cc.setCreateDate(NormalTools.curDate("yyyy-MM-dd"));
                cc.setCreateLong(System.currentTimeMillis());
                cc.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
                cc.setSchId(tea.getSchoolId());
                cc.setSchName(tea.getSchoolName());
                cc.setTeaId(tea.getId());
                cc.setTeaName(tea.getName());
                cc.setTeaPhone(tea.getPhone());
                cc.setContent(content);
                cc.setUrl(qiniuConfigTools.getQiniuConfig().getUrl() + key);
                classCommentDao.save(cc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

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
