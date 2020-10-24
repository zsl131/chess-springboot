package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 视频资源内容
 */
@Entity
@Table(name = "v_video_content")
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class VideoContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "cate_id")
    private Integer cateId;

    @Column(name = "cate_name")
    private String cateName;

    @Column(name = "order_no")
    private Integer orderNo;

    private String title;

    private String createDate;

    private String createTime;

    private Long createLong;

    private String showDate;

    public Long showDateLong;

    @Lob
    private String content;

    @Lob
    private String rawContent;

    private Integer readCount = 0;

    private String author;

    private String status = "1";
}
