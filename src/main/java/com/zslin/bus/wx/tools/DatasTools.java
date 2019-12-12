package com.zslin.bus.wx.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.INoticeDao;
import com.zslin.bus.basic.model.Notice;
import com.zslin.bus.common.rabbit.RabbitMQConfig;
import com.zslin.bus.wx.annotations.HasScore;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.ScoreAnnotation;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dao.IFeedbackDao;
import com.zslin.bus.wx.dao.IWxAccountDao;
import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.model.Feedback;
import com.zslin.bus.wx.model.WxAccount;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/24 22:26.
 */
@Component
@HasTemplateMessage
@HasScore
public class DatasTools {


    @Autowired
    private WxConfigTools wxConfigTools;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private WxAccountTools wxAccountTools;

    @Autowired
    private IWxAccountDao wxAccountDao;

//    @Autowired
//    private EventTools eventTools;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    @Autowired
    private IFeedbackDao feedbackDao;

    @Autowired
    private ExchangeTools exchangeTools;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private INoticeDao noticeDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /** 当用户取消关注时 */
    public void onUnsubscribe(String openid) {
        wxAccountDao.updateStatus(openid, "0");
    }

    /**
     * 添加文本内容
     * @param openid 用户Openid
     * @param builderName 开发者微信号
     * @param content 具体内容
     * @return
     */
    @TemplateMessageAnnotation(name = "在线反馈", keys = "反馈日期-反馈用户-反馈内容")
    public String onEventText(String openid, String builderName, String content) {
        if(content==null || "".equals(content.trim()) || "?".equals(content.trim())
                || "？".equals(content.trim()) || "1".equals(content.trim())
                || "help".equals(content.toLowerCase().trim())) { //帮助
            return WeixinXmlTools.createTextXml(openid, builderName, "HELP");
        } else {
            Feedback f = new Feedback();
            f.setCreateLong(System.currentTimeMillis());
            f.setCreateDate(DateTools.date2Str());
            f.setCreateTime(DateTools.date2Str("yyyy-MM-dd HH:mm:ss"));
            f.setOpenid(openid);
            f.setStatus("0");
            f.setType("text");
            f.setContent(content);
            WxAccount a = wxAccountDao.findByOpenid(openid);
            if (a != null) {
                f.setAccountId(a.getId());
                f.setNickname(a.getNickname());
                f.setHeadimgurl(a.getAvatarUrl());
            }
            feedbackDao.save(f);

//            List<String> adminOpenids = accountService.findOpenid(AccountTools.ADMIN);
            List<String> adminOpenids = wxAccountTools.getOpenid(wxAccountTools.ADMIN);
//            StringBuffer sb = new StringBuffer();
//            sb.append("反馈用户：").append(f.getNickname()).append(" \\n")
//                    .append("反馈内容：").append(content);
//            eventTools.eventRemind(adminOpenids, "在线反馈", "收到在线反馈信息", NormalTools.curDate("yyyy-MM-dd HH:mm"), sb.toString(), "/wx/feedback/list");
//            templateMessageTools.sendMessageByThread("在线反馈", adminOpenids, "/wx/feedback/list", "您有一条新的反馈信息！", "反馈日期="+NormalTools.curDate("yyyy-MM-dd HH:mm"), "反馈用户="+f.getNickname(), "反馈内容="+content);
            SendMessageDto smd = new SendMessageDto("在线反馈", adminOpenids, "/wx/feedback/list", "您有一条新的反馈信息！", "反馈日期="+NormalTools.curDate("yyyy-MM-dd HH:mm"), "反馈用户="+f.getNickname(), "反馈内容="+content);
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
            return "";
        }
    }

    /** 添加图片内容 */
    public void onEventImage(String openid, String picPath, String mediaId) {
        Feedback f = new Feedback();
        f.setCreateLong(System.currentTimeMillis());
        f.setCreateDate(DateTools.date2Str());
        f.setCreateTime(DateTools.date2Str("yyyy-MM-dd HH:mm:ss"));
        f.setOpenid(openid);
        f.setStatus("0");
        f.setType("image");
        f.setPicUrl(picPath);
        f.setMediaId(mediaId);
        f.setFilePath(exchangeTools.saveMedia(mediaId, configTools.getUploadPath("feedback/")+ UUID.randomUUID().toString()).replace(configTools.getUploadPath(), "\\"));
        WxAccount a = wxAccountDao.findByOpenid(openid);
        if(a!=null) {
            f.setAccountId(a.getId());
            f.setNickname(a.getNickname());
            f.setHeadimgurl(a.getAvatarUrl());
        }
        feedbackDao.save(f);

    }

    /** 当用户关注时 */
    public synchronized void onSubscribe(String openid) {
        Integer id = (Integer) wxAccountDao.queryByHql("SELECT a.id FROM WxAccount a WHERE a.openid=?", openid);
        WxAccount a ;
        boolean isFirst = false;
        if(id==null || id<=0) { //说明初次关注
            a = new WxAccount();
            a.setStatus("1");
            a.setType("0");
            a.setOpenid(openid);
            a.setCreateLong(System.currentTimeMillis());
            a.setCreateDate(DateTools.date2Str());
            a.setCreateTime(DateTools.date2Str("yyyy-MM-dd HH:mm:ss"));
            a.setFollowDate(DateTools.date2Str("yyyy-MM-dd HH:mm:ss"));
            isFirst = true;
        } else {
//            accountService.updateStatus(id, "1");
            a = wxAccountDao.findOne(id);
            if("0".equals(a.getStatus())) {
                a.setFollowDate(DateTools.date2Str("yyyy-MM-dd HH:mm:ss"));
                a.setStatus("1");
            }
        }
        JSONObject jsonObj = exchangeTools.getUserInfo(openid);
        if(jsonObj!=null) {
            String jsonStr = jsonObj.toString();
            if(jsonStr.indexOf("errcode")<0 && jsonStr.indexOf("errmsg")<0) {
                String nickname = "";
                try {
                    nickname = jsonObj.getString("nickname");
                    nickname = nickname.replaceAll("[^\\u0000-\\uFFFF]", ""); //替换utf8mb4字条
                } catch (Exception e) {
                }
                a.setAvatarUrl(jsonObj.getString("headimgurl"));
                a.setNickname(nickname);
                a.setOpenid(openid);
                a.setSex(jsonObj.getInt("sex")+"");
            }
        }
        wxAccountDao.save(a);

        if(isFirst) {
            plusScore(openid); //加积分
        }
    }

    @ScoreAnnotation("关注得积分")
    private void plusScore(String openid) {
        scoreTools.plusScoreByThread("关注得积分", openid);
    }

    /** 同步更新微信用户信息，主要是昵称、头像、性别 */
    public void updateAccount(Integer accountId) {
        WxAccount a = wxAccountDao.findOne(accountId);
        JSONObject jsonObj = exchangeTools.getUserInfo(a.getOpenid());
        if(jsonObj!=null) {
            String jsonStr = jsonObj.toString();
            if(jsonStr.indexOf("errcode")<0 && jsonStr.indexOf("errmsg")<0) {
                String nickname = "";
                try {
                    nickname = jsonObj.getString("nickname");
                    nickname = nickname.replaceAll("[^\\u0000-\\uFFFF]", ""); //替换utf8mb4字符
                } catch (Exception e) {
                }
                a.setAvatarUrl(jsonObj.getString("headimgurl"));
                a.setNickname(nickname);
                a.setSex(jsonObj.getInt("sex")+"");
            }
        }
        wxAccountDao.save(a);

//        updateRelation(a); //更新所有关联数据
    }

    /** 构建关注时的数据 */
    public String buildSubscribeStr(String toUser, String fromUser) {
        List<Notice> articleList = noticeDao.findNeedSend(SimpleSortBuilder.generateSort("orderNo_a"));
        return WeixinXmlTools.buildSubscribeStr(toUser, fromUser, articleList, wxConfigTools.getWxConfig().getUrl());
//        return "";
    }

    /*private void updateRelation(Account a) {
        commentService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
        feedbackService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
        activityRecordService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
        walletDetailService.update(a.getNickname(), a.getOpenid());
        qrcodeService.update(a.getNickname(), a.getHeadimgurl(), a.getOpenid());
    }*/

    /**
     * 检测在数据库中是否存在该用户
     * @param fromOpenid
     */
    public void checkWxAccount(String fromOpenid) {
        new Thread(()->onSubscribe(fromOpenid)).start();
    }
}
