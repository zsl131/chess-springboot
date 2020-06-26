package com.zslin.bus.wx.tools;

import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.model.ActivityRecordImage;
import com.zslin.bus.wx.dto.RecordImageCountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordImageTools {

    @Autowired
    private IActivityRecordDao activityRecordDao;

    public List<RecordImageCountDto> queryCountDto(List<ActivityRecordImage> data) {
        List<RecordImageCountDto> res = activityRecordDao.queryCountDto(buildRecordIds(data));
        return res;
    }

    /** 获取一组数据的记录ID */
    private Integer[] buildRecordIds(List<ActivityRecordImage> data) {
        Integer [] result = new Integer[data.size()];
        for(Integer i = 0; i<data.size(); i++) {
            result[i] = data.get(i).getRecordId();
        }
        return result;
    }
}
