package com.zslin.bus.share.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 推广图片
 */
@Entity
@Table(name = "s_share_img")
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ShareImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status = "0";

    /** 图片路径 */
    private String imgPath;

    /** 二维码起始X */
    private Integer startX;

    private Integer startY;

    private Integer endX;

    private Integer endY;

    /** 二维码宽度，endX - startX */
    private Integer qrWidth;

    /** 二维码高度，endY - startY */
    private Integer qrHeight;
}
