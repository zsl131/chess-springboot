package com.zslin.bus.share.service;

import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.share.dao.IShareUserQrDao;
import com.zslin.bus.share.model.ShareUserQr;
import com.zslin.bus.share.tools.BuildShareQrTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareUserQrService {

    @Autowired
    private IShareUserQrDao shareUserQrDao;

    @Autowired
    private BuildShareQrTools buildShareQrTools;

    public JsonResult buildQr(String params) {
        Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId"));
        buildShareQrTools.buildQr(recordId);
        List<ShareUserQr> list = shareUserQrDao.findByRecordId(recordId);
        return JsonResult.success().set("qrList", list);
    }

    public JsonResult listByRecordId(String params) {
        Integer recordId = Integer.parseInt(JsonTools.getJsonParam(params, "recordId"));
        List<ShareUserQr> list = shareUserQrDao.findByRecordId(recordId);
        return JsonResult.success().set("qrList", list);
    }
}
