package com.zslin.bus.basic.service;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.bus.basic.dao.IScoreRuleDao;
import com.zslin.bus.basic.model.ScoreRule;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.tools.ScoreAnnotationTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AdminAuth(name = "积分规则管理", psn = "系统管理", orderNum = 7, type = "1", url = "/admin/scoreRule")
public class ScoreRuleService {
    @Autowired
    private IScoreRuleDao scoreRuleDao;

    @Autowired
    private ScoreAnnotationTools scoreAnnotationTools;

    public JsonResult noConfig(String params) {
        List<String> list = scoreAnnotationTools.findNoConfigScore();
        return JsonResult.getInstance().set("list", list);
    }

    /** 全部已配置的数据 */
    public JsonResult list(String params) {
        List<ScoreRule> configed = scoreRuleDao.findAll();
        List<String> noConfig = scoreAnnotationTools.findNoConfigScore();
        return JsonResult.getInstance().set("noConfig", noConfig).set("configed", configed);
    }

    /*public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<ScoreRule> res = scoreRuleDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(), JsonParamTools.buildAuthDepSpe(params)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));
        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("datas", res.getContent());
    }*/

    public JsonResult addOrUpdate(String params) {
        ScoreRule s = JSON.toJavaObject(JSON.parseObject(params), ScoreRule.class);
        if(s.getId()==null || s.getId()<=0) {
            ScoreRule old = scoreRuleDao.findBySn(s.getSn());
            if(old!=null){
                return JsonResult.error("积分规则代码已经存在");
            }
            s.setSn(PinyinToolkit.cn2Spell(s.getName(), ""));
            scoreRuleDao.save(s);
        }else{
            ScoreRule old = scoreRuleDao.findOne(s.getId());
            old.setDayAmount(s.getDayAmount());
            old.setName(s.getName());
            old.setScore(s.getScore());
            old.setTotalAmount(s.getTotalAmount());
            scoreRuleDao.save(old);
            return JsonResult.success("修改成功");
        }
        return JsonResult.succ(s);
    }

    public JsonResult delete(String params){
        Integer id = Integer.parseInt(JsonTools.getJsonParam(params,"id"));
        scoreRuleDao.delete(id);
        return JsonResult.success("删除积分规则成功");
    }
}
