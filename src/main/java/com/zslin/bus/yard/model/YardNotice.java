package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

/**
 * 科普进校园公告
 */
@Entity
@Table(name = "y_yard_notice")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YardNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    private String status="0";

    @Column(name = "order_no")
    private Integer orderNo;
}
