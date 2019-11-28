package com.zslin.bus.common.controller;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IActivityStudentDao;
import com.zslin.bus.basic.model.ActivityStudent;
import com.zslin.bus.common.dto.QueryListConditionDto;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.dto.StudentCountDto;
import com.zslin.bus.tools.ExportStudentRecordCountTools;
import com.zslin.bus.tools.ExportStudentTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by zsl on 2018/11/29.
 */
@RestController
@RequestMapping(value = "api/download")
public class DownloadStudentController {

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private ExportStudentTools exportStudentTools;

    @Autowired
    private ExportStudentRecordCountTools exportStudentRecordCountTools;

    private DecimalFormat df = new DecimalFormat("#.00");

    /**
     * 下载学生报名统计
     * @param request
     * @param recordId 活动记录ID
     * @param response
     * @return
     */
    @GetMapping(value = "countRecord")
    public @ResponseBody String countRecord(HttpServletRequest request, Integer recordId, HttpServletResponse response) {
        try {
            if(recordId==null || recordId<=0) {
                recordId = (Integer) activityStudentDao.queryByHql("SELECT s.recordId FROM ActivityStudent s WHERE s.id=(SELECT MAX(a.id) FROM ActivityStudent a )");
            }
            response.setCharacterEncoding("UTF-8");
            String fileName = "学生报名统计导出-"+ NormalTools.curDate("yyyyMMdd")+".xlsx";
//            fileName = URLEncoder.encode(fileName,"UTF-8");
            response.setContentType("application/x-excel");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes(), "ISO-8859-1"));
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "ISO-8859-1"));

            List<QueryListConditionDto> conList = buildQuery(request);
            List<ActivityStudent> data = activityStudentDao.listByHql("FROM ActivityStudent s WHERE s.stuName in (SELECT a.stuName FROM ActivityStudent a WHERE a.recordId=?1) ORDER BY s.stuName", recordId);
            exportStudentRecordCountTools.exportStudent(buildData(data), response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败："+e.getMessage();
        }
        return "下载成功";
    }

    private StudentCountDto buildDto(String name, String phone, Integer total, Integer signCount, Integer passCount) {
        StudentCountDto dto = new StudentCountDto();
        dto.setName(name);
        dto.setPhone(phone);
        dto.setSignCount(signCount);
        dto.setPassCount(passCount);
        dto.setTotal(total);
        dto.setSignPercent(passCount==0?0:Double.parseDouble(df.format((signCount*1.0d/passCount*1.0d))));

        return dto;
    }

    private List<StudentCountDto> buildData(List<ActivityStudent> data) {
        List<StudentCountDto> result = new ArrayList<>();
        String name= "";
        int count = 0, passCount = 0, signCount = 0;
        int index = 0;
        for(ActivityStudent stu : data) {
            if(!stu.getStuName().equals(name)) { //如果姓名不同则换人
                if(!"".equals(name)) {
                    /*StudentCountDto dto = new StudentCountDto();
                    dto.setName(name);
                    dto.setPhone(stu.getPhone());
                    dto.setSignCount(signCount);
                    dto.setPassCount(passCount);
                    dto.setTotal(count);
                    dto.setSignPercent(passCount==0?0:Double.parseDouble(df.format((signCount*1.0/passCount*1.0d))));
                    result.add(dto);*/
                    result.add(buildDto(name, stu.getPhone(), count, signCount, passCount));
                }
                name = stu.getStuName(); count = 0; passCount = 0;signCount=0;
            }
            count ++;
            if("1".equals(stu.getStatus())) {passCount ++;}
            if("1".equals(stu.getHasCheck())) {signCount++;}

            if((index++)>=data.size()-1) {
                result.add(buildDto(name, stu.getPhone(), count, signCount, passCount));
            }
        }
        return result;
    }

    @GetMapping(value = "student")
    public @ResponseBody
    String student(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            String fileName = "报名信息导出-"+ NormalTools.curDate("yyyyMMdd")+".xlsx";
//            fileName = URLEncoder.encode(fileName,"UTF-8");
            response.setContentType("application/x-excel");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String("用户列表.xls".getBytes(), "ISO-8859-1"));
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "ISO-8859-1"));

            List<QueryListConditionDto> conList = buildQuery(request);
            List<ActivityStudent> data = activityStudentDao.findAll(QueryTools.getInstance().buildSearch(conList));
            exportStudentTools.exportStudent(data, response.getOutputStream());
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
