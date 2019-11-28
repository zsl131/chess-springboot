package com.zslin.bus.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2019/9/24.
 */
@Entity
@Table(name = "app_feedback_img")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppFeedbackImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "random_id")
    private String randomId;

    /** 用于区分有没有对应的反馈信息，如果没有则需要把图片删除，1-已对应；0-未对应 */
    private String flag = "0";

    private String url;

    @Column(name = "create_long")
    private Long createLong;

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
