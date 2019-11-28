package com.zslin.bus.common.controller;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.tools.ExportStudentTools;
import com.zslin.bus.yard.dao.IClassSystemDao;
import com.zslin.bus.yard.dao.IClassSystemDetailDao;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/12/25.
 */
@RestController
@RequestMapping(value = "api/download")
public class DownloadSystemController {

    @Autowired
    private ExportStudentTools exportStudentTools;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @GetMapping(value = "system")
    public @ResponseBody
    String student(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            String fileName = "课程体系导出-"+ NormalTools.curDate("yyyyMMdd")+".xlsx";
            response.setContentType("application/x-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "ISO-8859-1"));

            String sidStr = request.getParameter("sid");
            String type = request.getParameter("type");
            String orderBy = request.getParameter("orderBy");
            exportStudentTools.export("课程体系导出-"+NormalTools.curDate(), "课程体系", buildData(orderBy, sidStr, type),
                    new String[]{"体系","学期", "分类", "小分类", "名称", "学习目标", "章节","课程", "排序", "课标范围"},
                    new String[]{"spname", "sname", "cpname", "cname", "name", "courseTarget", "sectionNo", "courseTitle", "orderNo", "inRange__inRange"},
                    response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败："+e.getMessage();
        }
        return "下载成功";
    }

    private List<ClassSystemDetail> buildData(String orderBy, String sidStr, String type) {
        Integer sid =0;
        try { sid = Integer.parseInt(sidStr); } catch (Exception e) { }

        Sort sort = SimpleSortBuilder.generateSort(orderBy);
        Sort rootSort = SimpleSortBuilder.generateSort("orderNo_a");
        List<ClassSystemDetail> result = new ArrayList<>();
        if("all".equalsIgnoreCase(type)) {
            List<ClassSystem> rootList = classSystemDao.findRoot(rootSort);
            for (ClassSystem root : rootList) {
                List<ClassSystem> children = classSystemDao.findByParent(root.getId(), rootSort);
                for (ClassSystem child : children) {
                    result.addAll(classSystemDetailDao.findBySid(child.getId(), sort));
                }
            }
        } else if("root".equalsIgnoreCase(type)) {
            List<ClassSystem> children = classSystemDao.findByParent(sid, rootSort);
            for (ClassSystem child : children) {
                result.addAll(classSystemDetailDao.findBySid(child.getId(), sort));
            }
        } else if("child".equalsIgnoreCase(type)) {
            result.addAll(classSystemDetailDao.findBySid(sid, sort));
        }
        return result;
    }
}
