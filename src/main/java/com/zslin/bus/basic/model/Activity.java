package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/7/12.
 * 活动信息
 */
@Entity
@Table(name = "t_activity")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    /**
     * 格式如： yyyy-MM-dd
     */
    @Column(name = "create_date")
    private String createDate;

    /**
     * 格式如：yyyy-MM-dd HH:mm:ss
     */
    @Column(name = "create_time")
    private String createTime;

    @Lob
    private String content;

    /**
     * 管理部门id
     */
    @Column(name = "dep_id")
    private Integer depId;

    /**
     * 管理部门名称
     */
    @Column(name = "dep_name")
    private String depName;

    private String status;

    /** 图片路径 */
    @Column(name = "img_url")
    private String imgUrl;

    /** 阅读次数 */
    @Column(name = "read_count")
    private Integer readCount=0;

    /** 点赞次数 */
    @Column(name = "good_count")
    private Integer goodCount=0;

    /** 评论次数 */
    @Column(name = "comment_count")
    private Integer commentCount=0;

    /** 活动开展次数 */
    @Column(name = "record_count")
    private Integer recordCount = 0;

    /** 导读 */
    @Lob
    private String guide;

    /** 是否可报名参加，为1时表示开启报名通道 */
    @Column(name = "can_join")
    private String canJoin = "0";

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public String getCanJoin() {
        return canJoin;
    }

    public void setCanJoin(String canJoin) {
        this.canJoin = canJoin;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }
}
