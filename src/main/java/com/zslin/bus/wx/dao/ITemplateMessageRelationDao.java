package com.zslin.bus.wx.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.wx.model.TemplateMessageRelation;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl on 2018/8/28.
 */
public interface ITemplateMessageRelationDao extends BaseRepository<TemplateMessageRelation, Integer> {

    TemplateMessageRelation findByTemplatePinyin(String templatePinyin);

    @Query("SELECT templateId FROM TemplateMessageRelation WHERE templatePinyin=?1")
    String findTemplateIdByTemplatePinyin(String templatePinyin);
}
