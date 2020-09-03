package com.zslin.bus.common.controller;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.yard.dao.IAttachmentDao;
import com.zslin.bus.yard.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by zsl on 2018/11/29.
 */
@RestController
@RequestMapping(value = "api/download")
public class DownloadAttachmentController {


    @Autowired
    private IAttachmentDao attachmentDao;

    @Autowired
    private ConfigTools configTools;

    @GetMapping(value = "attachment")
    public @ResponseBody
    String attachment(HttpServletRequest request, Integer attaId, HttpServletResponse response) {
        OutputStream outputStream = null;
        BufferedInputStream bis = null;
        try {
            Attachment attachment = attachmentDao.findOne(attaId);
            if(attachment==null) {
                return "";
            }
            response.setCharacterEncoding("UTF-8");
            String fileName = attachment.getFileName();
//            fileName = URLEncoder.encode(fileName,"UTF-8");
//            response.setContentType("application/x-excel");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes(), "ISO-8859-1"));
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "ISO-8859-1"));

            outputStream = response.getOutputStream();
            File file = new File(configTools.getUploadPath()+attachment.getUrl());
            if(!file.exists()) {
                System.out.println("文件不存在 ：" +file.getAbsolutePath());
                return "";
            }

            int length = 1024;
            byte[] buff = new byte[1024];

            bis = new BufferedInputStream(new FileInputStream(file));

            while((length=bis.read(buff))!=-1) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();


        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败："+e.getMessage();
        } finally {
            try {
                if(bis!=null) {bis.close();}
            } catch (IOException e) {
            }
            try {
                if(outputStream!=null) {outputStream.close();}
            } catch (IOException e) {
            }
        }
        return "1";
    }
}
