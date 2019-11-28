package com.zslin.bus.yard.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * Created by zsl on 2018/9/12.
 * 附件
 */
@Entity
@Table(name = "y_attachment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String url;

    /**
     * 类型，1-图片；2-ppt；3-视频；4-Word文档；5-PDF
     */
    private String type;

    /**
     * 文件格式，如：mp4、ppt、jpg
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 文件大小，单位B
     */
    @Column(name = "file_size")
    private long fileSize;

    /**
     * 时长，单位毫秒，只有视频才有此属性
     */
    @Column(name = "time_length")
    private long timeLength;

    /** 上传之前的文件名 */
    @Column(name = "file_name")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(long timeLength) {
        this.timeLength = timeLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

}
