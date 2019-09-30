package com.ry.community.model;

public class Notification {
    private Long id;

    private Long notifier;

    private Long receiver;

    private Integer type;

    private Long gmtCreate;

    private Integer status;

    private Long outerid;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotifier() {
        return notifier;
    }

    public void setNotifier(Long notifier) {
        this.notifier = notifier;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOuterid() {
        return outerid;
    }

    public void setOuterid(Long outerid) {
        this.outerid = outerid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}