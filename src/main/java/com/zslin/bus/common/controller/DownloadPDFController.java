package com.zslin.bus.common.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.finance.tools.TicketNoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zsl on 2019/1/5.
 */
@RestController
@RequestMapping(value = "api/downloadPdf")
public class DownloadPDFController {

    @Autowired
    private TicketNoTools ticketNoTools;

    @Autowired
    private ConfigTools configTools;

    @GetMapping(value = {"", "/", "index"})
    public void index(HttpServletRequest request, HttpServletResponse response, String month) {
        response.setHeader("content-type", "application/pdf");
        response.setContentType("application/pdf");

        String no1 = ticketNoTools.buildTicketNo(month);
        String no2 = ticketNoTools.buildTicketNo(month);
        String name = month + no1 +"-"+no2+".pdf";

        response.setHeader("Content-Disposition", "attachment;filename="+name);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            createPDF(os, month+no1, month+no2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPDF(OutputStream os, String tno1, String tno2) {
        // 模板路径
//        String templatePath = "D:/temp/1111.pdf";
        // 生成的新文件路径
//        String newPDFPath = "C:/myuser/债权债务确认函样板.pdf";
        PdfReader reader;
//        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
//            out = new FileOutputStream(newPDFPath);// 输出流
//            File templateFile = ResourceUtils.getFile("classpath:public/finance_ticket_template.pdf"); //File
            File templateFile = new File(configTools.getUploadPath("/finance_ticket_template.pdf"));
            reader = new PdfReader(templateFile.getAbsolutePath());// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            /*Map<String, AcroFields.Item> map = form.getFields();
            for(String key : map.keySet()) {
                System.out.println(key+"======="+map.get(key).toString());
            }*/

            form.setField("ticketNo1", tno1);
            form.setField("ticketNo2", tno2);

            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, os);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (Exception e) {
            System.out.println(1);
        }
    }
}
