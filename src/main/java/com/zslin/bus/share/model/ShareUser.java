package com.zslin.bus.share.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_share_user")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ShareUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String openid;

    private Integer accountId;

    private String name;

    private String phone;

    private String createTime;

    private String createDate;

    private Long createLong;
}
