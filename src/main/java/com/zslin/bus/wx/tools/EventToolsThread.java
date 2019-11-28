package com.zslin.bus.wx.tools;

import com.zslin.bus.wx.dto.EventRemarkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zsl on 2018/8/2.
 */
@Component
public class EventToolsThread {

    @Autowired
    private EventTools eventTools;

    /**
     * 事件提醒
     * @param toUser 接收者
     * @param title 事件标题
     * @param eventType 事件类型
     * @param date 日期
     * @param remark 提醒具体内容
     * @param url 链接地址
     * @return
     */
    public boolean eventRemind(String toUser,
                            String title,
                            String eventType,
                            String date,
                            String remark,
                            String url) {
        new Thread(() -> eventTools.eventRemind(toUser, title, eventType, date, remark, url)).start();
        return true;
    }

    public void eventRemind(List<String> users, String title, String eventType, String date, String url, EventRemarkDto... dtos) {
        new Thread(() ->
            eventTools.eventRemind(users, title, eventType, date, url, dtos)
        ).start();
    }
}
