package com.zslin.bus.common.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IDraftDao;
import com.zslin.bus.basic.model.Draft;
import com.zslin.bus.common.dto.QueryListConditionDto;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.ExportDraftTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by zsl on 2018/11/29.
 */
@RestController
@RequestMapping(value = "api/download")
public class DownloadDraftController {

    @Autowired
    private ExportDraftTools exportDraftTools;

    @Autowired
    private IDraftDao draftDao;

    @GetMapping(value = "draft")
    public @ResponseBody
    String student(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            String fileName = "视频文稿导出-"+ NormalTools.curDate("yyyyMMdd")+".xlsx";
//            fileName = URLEncoder.encode(fileName,"UTF-8");
            response.setContentType("application/x-excel");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes(), "ISO-8859-1"));
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "ISO-8859-1"));

            List<QueryListConditionDto> conList = buildQuery(request);
            List<Draft> data = draftDao.findAll(QueryTools.getInstance().buildSearch(conList));
            exportDraftTools.exportData(data, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败："+e.getMessage();
        }
        return "下载成功";
    }

    private List<QueryListConditionDto> buildQuery(HttpServletRequest request) {
        List<QueryListConditionDto> result = new ArrayList<>();
        Enumeration<String> names = request.getParameterNames();
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
//            System.out.println(name+"===="+value);
            if(name.startsWith("_")) {
                result.add(new QueryListConditionDto(name.substring(1,name.length()), "eq", value));
            }
            String key , match ;
            if(name.indexOf("_")>0) {
                key = name.split("_")[0];
                match = name.split("_")[1];
            } else {
                key = name; match = "eq";
            }
            if(value!=null && !"".equals(value.trim()) && !"*".equals(value) && !"?".equals(value)) { //星号问号需要忽略
                result.add(new QueryListConditionDto(key, match, value));
            }
        }
        return result;
    }
}
