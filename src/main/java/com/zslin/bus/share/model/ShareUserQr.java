package com.zslin.bus.share.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_share_user_qr")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ShareUserQr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 对应的活动ID */
    private Integer recordId;

    /** 对应的活动标题 */
    private String activityTitle;

    /** 二维码地址 */
    private String qrPath;

    /** 对应的ShareUser的ID */
    private Integer userId;

    /** 对应的用户姓名 */
    private String name;

    /** 对应的用户手机号码 */
    private String phone;
}
