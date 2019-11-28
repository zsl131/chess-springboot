package com.zslin.bus.wx.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/8/28.
 *
 */
@Entity
@Table(name = "wx_template_message_relation")
public class TemplateMessageRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 模板名称，与本系统中名称一一对应 */
    @Column(name = "template_name")
    private String templateName;

    /** 名称拼音 */
    @Column(name = "template_pinyin")
    private String templatePinyin;

    /** 模板ID，与微信平台里的ID一一对应 */
    @Column(name = "template_id")
    private String templateId;

    /** 键值对，反馈日期=keyword1-回复内容=keyword2-备注=remark */
    @Lob
    @Column(name = "key_values")
    private String keyValues;

    public String getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(String keyValues) {
        this.keyValues = keyValues;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplatePinyin() {
        return templatePinyin;
    }

    public void setTemplatePinyin(String templatePinyin) {
        this.templatePinyin = templatePinyin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
