package com.zslin.bus.sms.service;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.sms.dao.ISendRecordDao;
import com.zslin.bus.sms.model.SendRecord;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.tools.SmsTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/10/9.
 */
@Service
@AdminAuth(name = "短信发送记录", psn = "短信管理", url = "/admin/sendRecord", type = "1", orderNum = 2)
public class SendRecordService {

    @Autowired
    private ISendRecordDao sendRecordDao;

    @Autowired
    private SmsTools smsTools;

    public JsonResult list(String params) {
        QueryListDto qld = QueryTools.buildQueryListDto(params);
        Page<SendRecord> res = sendRecordDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList()),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        Integer surplus = smsTools.surplus(); //剩余条件
        return JsonResult.getInstance().set("size", (int) res.getTotalElements()).set("data", res.getContent()).set("surplus", surplus);
    }
}
