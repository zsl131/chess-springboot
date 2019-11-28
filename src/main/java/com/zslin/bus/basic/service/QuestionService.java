package com.zslin.bus.basic.service;


import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.basic.dao.IQuestionDao;
import com.zslin.bus.basic.model.Question;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AdminAuth(name = "活动题目管理", psn = "系统管理", url = "/admin/question", type = "1", orderNum = 5)
public class QuestionService {
    @Autowired
    private IQuestionDao questionDao;


    public JsonResult list(String params) {
        try {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<Question> res = questionDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
    } catch (Exception e) {
        e.printStackTrace();
        return JsonResult.error(e.getMessage());
    }
    }

    public JsonResult addOrUpdate(String params) {
        Question q = JSON.toJavaObject(JSON.parseObject(params),Question.class);
        /**修改*/
        if(q.getId()!=null && q.getId()>0) {
            Question a = questionDao.findOne(q.getId());
            a.setContent(q.getContent());
            a.setType(q.getType());
            if(q.getType().equals("判断题")) {
                q.setA("正确");
                q.setB("错误");
                q.setC("");
                q.setD("");
                if(q.getReply().equals("C") || q.getReply().equals("D")) {
                    return JsonResult.error("不存在CD选项");
                } else {
                    questionDao.save(q);
                    return JsonResult.success("保存成功");
                }
            } else {
                a.setA(q.getA());
                a.setB(q.getB());
                a.setC(q.getC());
                a.setD(q.getD());
                a.setType(q.getType());
                a.setReply(q.getReply());
                questionDao.save(a);
                return JsonResult.success("修改成功");
            }
            /**添加*/
        } else if(q.getType().equals("判断题")){
            q.setA("正确");
            q.setB("错误");
            q.setC("");
            q.setD("");
            if(q.getReply().equals("C") || q.getReply().equals("D")) {
                return JsonResult.error("不存在CD选项");
            } else {
                questionDao.save(q);
                return JsonResult.success("保存成功");
            }
        } else if((q.getA()!=null && q.getB()!=null) && (q.getC()!=null && q.getD()!=null)){
            questionDao.save(q);
            return JsonResult.success("保存成功");
        } else {
            return JsonResult.error("选项不能为空");
        }
    }
    public JsonResult delete(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
            questionDao.delete(id);
            return JsonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
    public JsonResult loadOne(String params) {
        try {
            Integer id = Integer.parseInt(JsonTools.getJsonParam(params, "id"));
            Question obj = questionDao.findOne(id);
            return JsonResult.succ(obj);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }
}
