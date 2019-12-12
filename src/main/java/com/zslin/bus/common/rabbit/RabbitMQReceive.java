package com.zslin.bus.common.rabbit;

import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE) //监听的队列名称 TestDirectQueue
public class RabbitMQReceive {

    @Autowired
    private TemplateMessageTools templateMessageTools;

    /** 处理模板消息 */
    @RabbitHandler
    public void handlerSendMessage(SendMessageDto dto) {
//        System.out.println("==============================");
//        System.out.println(dto);
        templateMessageTools.sendMessageByDto(dto);
    }
}
