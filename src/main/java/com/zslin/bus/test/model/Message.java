package com.zslin.bus.test.model;

import javax.persistence.*;

@Entity
@Table(name = "test_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer user;
    private String title;
    private String content;
    private String date;


    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
