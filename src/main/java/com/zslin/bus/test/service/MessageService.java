package com.zslin.bus.test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zslin.bus.test.dao.IMessageDao;
import com.zslin.bus.test.model.Message;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageService {
    @Autowired
    private IMessageDao messageDao;
    public JsonResult add(String params){
        Message m = JSONObject.toJavaObject(JSON.parseObject(params),Message.class);
        messageDao.save(m);
        return JsonResult.succ(m);
    }
    public JsonResult List(String params){
        System.out.println("messageService.list->params:::");
        List<Message> list = messageDao.findAll();
        return JsonResult.getInstance().set("size", list.size()).set("datas", list);
    }
}
