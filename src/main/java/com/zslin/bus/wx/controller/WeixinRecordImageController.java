package com.zslin.bus.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.dao.IActivityRecordImageDao;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.basic.model.ActivityRecordImage;
import com.zslin.bus.wx.dto.RecordImageCountDto;
import com.zslin.bus.wx.tools.RecordImageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "weixin/recordImage")
@Controller
public class WeixinRecordImageController {

    @Autowired
    private IActivityRecordDao activityRecordDao;

    @Autowired
    private IActivityRecordImageDao activityRecordImageDao;

    @Autowired
    private RecordImageTools recordImageTools;

    private static final String TEMP_PREFIX = "weixin/recordImage/";

    /** 影集列表，新首页 */
    @GetMapping(value = "list")
    public String list(Model model, Integer page) {
        page = (page==null||page<=0)?0:page;
        Page<ActivityRecordImage> data = activityRecordImageDao.find4Page(
                SimplePageBuilder.generate(page, 15, SimpleSortBuilder.generateSort("recordHoldTimeLong_d", "recordId_d")));
        List<RecordImageCountDto> countList = recordImageTools.queryCountDto(data.getContent());
        model.addAttribute("datas", data);
        System.out.println(countList);
        model.addAttribute("countList", countList);

        return TEMP_PREFIX + "list";
    }

    @GetMapping(value = {"index", "", "/"})
    public String index(Model model, Integer page, Integer recordId, HttpServletRequest request) {
        page = (page==null||page<=0)?0:page;
        Page<ActivityRecordImage> datas = activityRecordImageDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request,
                (recordId!=null&&recordId>0)?new SpecificationOperator("recordId", "eq", recordId):null),
                SimplePageBuilder.generate(page, 20, SimpleSortBuilder.generateSort("id_d")));
        List<List<ActivityRecordImage>> result = rebuildData(datas.getContent());
        model.addAttribute("data1", result.get(0));
        model.addAttribute("data2", result.get(1));
        model.addAttribute("datas", datas);
        model.addAttribute("current", 1);
        model.addAttribute("record", activityRecordDao.findOne(recordId));
        return TEMP_PREFIX + "index";
    }

    /** 重新构建数据 */
    private List<List<ActivityRecordImage>> rebuildData(List<ActivityRecordImage> list) {
        List<ActivityRecordImage> list1 = new ArrayList<>();
        List<ActivityRecordImage> list2 = new ArrayList<>();
        int i=0;
        for(ActivityRecordImage a : list) {
            if((i++)%2==0) {
                list2.add(a);
            } else {list1.add(a);}
        }
        List<List<ActivityRecordImage>> result = new ArrayList<>();
        result.add(list1);
        result.add(list2);
        return result;
    }

    @GetMapping(value = "listRecord")
    public String listRecord(Model model, Integer page, HttpServletRequest request) {
        page = (page==null||page<=0)?0:page;
        Page<ActivityRecord> datas = activityRecordDao.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("current", 2);
        return TEMP_PREFIX + "listRecord";
    }
}
