package com.zslin.bus.basic.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 视频资源分类
 */
@Entity
@Table(name = "v_video_category")
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class VideoCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 序号 */
    @Column(name = "order_no")
    private Integer orderNo;

    private String name;

    @Lob
    private String remark;

    @Column(name = "pic_path")
    private String picPath;

    private String status = "1";
}
