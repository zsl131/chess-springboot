package com.zslin.bus.yard.controller;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.excel.ExcelImportUtils;
import com.zslin.bus.qiniu.dto.MyPutRet;
import com.zslin.bus.qiniu.tools.QiniuConfigTools;
import com.zslin.bus.qiniu.tools.QiniuUploadTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.IAttachmentDao;
import com.zslin.bus.yard.dao.ISchoolDao;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.model.Attachment;
import com.zslin.bus.yard.model.School;
import com.zslin.bus.yard.model.Teacher;
import com.zslin.bus.yard.tools.MyFileTools;
import com.zslin.bus.yard.tools.TeacherRoleTools;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * Created by zsl on 2018/9/12.
 */
@RestController
@RequestMapping(value = "api/yardUpload")
public class YardUploadController {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private ISchoolDao schoolDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private TeacherRoleTools teacherRoleTools;

    @Autowired
    private QiniuUploadTools qiniuUploadTools;

    @Autowired
    private QiniuConfigTools qiniuConfigTools;

    private static final String UPLOAD_PATH_PRE = "/publicFile/yard";

    /**
     * 保存文件，直接以multipartFile形式
     * @param multipartFile
     * @param path 文件保存绝对路径
     * @return 返回文件名
     * @throws IOException
     */
    @RequestMapping(value = "uploadFile")
    public JsonResult uploadFile(@RequestParam("file")MultipartFile[] multipartFile, String path) throws IOException {
        if(multipartFile!=null && multipartFile.length>=1) {
            MultipartFile mf = multipartFile[0];
//                System.out.println(mf.getName() + "===" + mf.getOriginalFilename() + "===" + mf.getContentType());
            FileInputStream fileInputStream = (FileInputStream) mf.getInputStream();

            MultipartFile file = multipartFile[0];
            String fileName = file.getOriginalFilename();
//                    System.out.println("========fileName::"+fileName);
            if (fileName != null && !"".equalsIgnoreCase(fileName.trim())) {
                String fileType = MyFileTools.getFileType(fileName);

                boolean isVideo = false;
                String type = "";//1-图片；2-ppt；3-视频；4-Word文档；5-PDF
                if(MyFileTools.isImageFile(fileName)) {type = "1";}
                else if(MyFileTools.isPPTFile(fileName)) {type = "2";}
                else if(MyFileTools.isVideoFile(fileName)) {type = "3"; isVideo = true;}
                else if(MyFileTools.isWordFile(fileName)) {type = "4";}
                else if(MyFileTools.isPDFFile(fileName)) {type = "5";}

                Attachment atta = new Attachment();

                if(!isVideo) {
                    File outFile = new File(configTools.getUploadPath(UPLOAD_PATH_PRE) + File.separator + path + File.separator + UUID.randomUUID().toString() + fileType);
                    String filePath = outFile.getAbsolutePath().replace(configTools.getUploadPath(), File.separator);
                    FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
                    atta.setUrl(filePath);
                } else { //是视频
                    String key = System.currentTimeMillis() + fileType.toLowerCase();
                    atta.setUrl(qiniuConfigTools.getQiniuConfig().getUrl() + key);
                    MyPutRet mpr = qiniuUploadTools.upload(file.getInputStream(), key);
                    String timeLen = mpr.getTimeLen();
                    if(timeLen!=null && !"".equals(timeLen)) {
                        Double d = Double.parseDouble(timeLen) * 1000;
                        atta.setTimeLength(d.longValue());
                    } else {
                        atta.setTimeLength(0l);
                    }
                }
//                result.add(uploadPath);

                atta.setFileType(fileType.toLowerCase());
                atta.setFileSize(file.getSize());

                atta.setFileName(fileName);
                atta.setType(type);
                /*if(isVideo) {
                    atta.setTimeLength(MyFileTools.getVideoTimeLength(outFile));
                }*/
                attachmentDao.save(atta);
//                result = atta.getId()+"-"+filePath; //组合结果
                return JsonResult.succ(atta);
            }
        }
        return JsonResult.error("上传不成功");
    }

    @RequestMapping(value = "importTeachers")
    public String importTeachers(@RequestParam("file")MultipartFile[] multipartFile,Integer schoolId) throws IOException {
//        System.out.println("======="+path+"=====length:"+multipartFile.length);
        School school = schoolDao.findOne(schoolId);
        String result = "error";
        if(multipartFile!=null && multipartFile.length>=1) {
            result = "完成";
            MultipartFile mf = multipartFile[0];
            FileInputStream fileInputStream = (FileInputStream) mf.getInputStream();

            MultipartFile file = multipartFile[0];
            String fileName = file.getOriginalFilename();
            if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && ExcelImportUtils.validateExcel(fileName)) {
                //根据版本选择创建Workbook的方式
                Workbook wb = null;
                //根据文件名判断文件是2003版本还是2007版本
                if(ExcelImportUtils.isExcel2007(fileName)){
                    wb = new XSSFWorkbook(file.getInputStream());
                }else{
                    wb = new HSSFWorkbook(file.getInputStream());
                }

                int sucCount = 0;

                //得到第一个shell
                Sheet sheet=wb.getSheetAt(0); //从第三行开始才是内容
                for(int i=2;i<sheet.getLastRowNum();i++) {
                    try {
                        Row row = sheet.getRow(i);
                        String name = removeBlank(row.getCell(0).getStringCellValue()); //姓名
                        String sex = removeBlank(row.getCell(1).getStringCellValue()); //性别
                        String phone = removeBlank(row.getCell(2).getStringCellValue()); //手机号码
                        String identity = removeBlank(row.getCell(3).getStringCellValue()); //身份证号
                        String remark = removeBlank(row.getCell(4).getStringCellValue()); //备注
                        sex = ("1".equals(sex) || "男".equals(sex))?"1":"2"; //性别
                        if(isNull(name) || isNull(phone)) {continue;}
                        if(teacherDao.findByPhone(phone)!=null) {continue;} //同一个手机号码不能重复添加
                        Teacher t = new Teacher();
                        t.setName(name);
                        t.setIdentity(identity);
                        t.setPhone(phone);
                        t.setRemark(remark);
                        t.setSex(sex);
                        t.setSchoolId(schoolId);
                        t.setSchoolName(school.getName());
                        teacherDao.save(t); //保存

                        teacherRoleTools.setTeacherRole(t); //设置用户信息
                        sucCount ++;
                    } catch (Exception e) {
                    }
                }
                result = "导入完成！成功导入【"+sucCount+"】条教师信息。";
            }
        }
        return result;
    }

    private String removeBlank(String value) {
        if(isNull(value)) {return null;}
        return value.replaceAll(" ", "").replace("　", "").trim();
    }

    private boolean isNull(String value) {
        return value==null || "".equals(value.trim());
    }

}
