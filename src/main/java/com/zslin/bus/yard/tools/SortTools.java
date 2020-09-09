package com.zslin.bus.yard.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.yard.dao.ITeachPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拖动排序工具类
 */
@Component
public class SortTools {

    @Autowired
    private ITeachPlanDao teachPlanDao;

    public void handler(String type, String sortArrayJson) {
        JSONArray jsonArray = JsonTools.str2JsonArray(sortArrayJson);
        for(int i=0;i<jsonArray.size();i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
//            System.out.println(obj.getString("name")+"------->"+obj.getInteger("orderNo"));
            String hql = "UPDATE "+type+" o SET o.orderNo=?1 WHERE o.id=?2 ";
            teachPlanDao.updateByHql(hql, obj.getInteger("orderNo"), obj.getInteger("id"));
        }
    }
}
