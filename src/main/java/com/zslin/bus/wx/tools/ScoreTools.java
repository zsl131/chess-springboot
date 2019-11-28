package com.zslin.bus.wx.tools;

import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.bus.basic.dao.IScoreRecordDao;
import com.zslin.bus.basic.dao.IScoreRuleDao;
import com.zslin.bus.basic.model.ScoreRecord;
import com.zslin.bus.basic.model.ScoreRule;
import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import com.zslin.bus.wx.dao.IWxAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/9/3.
 * 积分处理工具类
 */
@Component
@HasTemplateMessage
public class ScoreTools {

    @Autowired
    private IScoreRuleDao scoreRuleDao;

    @Autowired
    private IScoreRecordDao scoreRecordDao;

    @Autowired
    private IWxAccountDao wxAccountDao;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    public void plusScoreByThread(String name, String openid) {
        new Thread(()-> plusScore(name, openid)).start();
    }

    @TemplateMessageAnnotation(name = "增加积分通知", keys = "增加原因-增加分值-处理时间")
    public void plusScore(String name, String openid) {
        if(name==null || "".equals(name.trim()) || openid==null || "".equals(openid.trim())) {return;} //如果参数未传，则忽略
        String sn = getSn(name);
        ScoreRule sr = scoreRuleDao.findBySn(sn);
        if(sr==null) return;
        Integer totalAmount = scoreRecordDao.findTotalAmountByOpenid(openid, sn);
        Integer dayAmount = scoreRecordDao.findDayAmountByOpenid(openid, sn, NormalTools.curDate("yyyy-MM-dd"));
        Integer ta = sr.getTotalAmount(); Integer da = sr.getDayAmount();
        if((ta<0 || totalAmount<ta) && (da<0 || dayAmount<da)) { //totalAmount无限次数或者次数未达标
            plusScore(openid, sr);
            //暂时不要通过积分变化
            /*templateMessageTools.sendMessageByThread("增加积分通知", openid, "#", "您的积分又增加了",
                    TemplateMessageTools.field("增加原因", name),
                    TemplateMessageTools.field("增加分值", sr.getScore()+""),
                    TemplateMessageTools.field("处理时间", NormalTools.curDate()));*/
        }
    }

    private void plusScore(String openid, ScoreRule rule) {
        ScoreRecord sr = new ScoreRecord();
        sr.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
        sr.setCreateTime(NormalTools.curDatetime());
        sr.setOpenid(openid);
        sr.setReason(rule.getName());
        sr.setReasonSn(rule.getSn());
        scoreRecordDao.save(sr);

        //修改积分
        wxAccountDao.updateScore(openid, rule.getScore());
    }

    /** 获取SN */
    private String getSn(String name) {
        return PinyinToolkit.cn2Spell(name, "");
    }
}
