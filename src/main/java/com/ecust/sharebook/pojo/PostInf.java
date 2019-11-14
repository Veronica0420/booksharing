package com.ecust.sharebook.pojo;

import java.util.Date;

public class PostInf {
    private Integer postId;

    private Integer publisherId;

    private Date pubTime;

    private String title;

    private String content;

    private Integer bookCircleId;

    private  UserInf userInf;

    private Integer count;

    private int flag; //0 自己  1朋友 2 陌生人

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Date getPubTime() {
        return pubTime;
    }

    public void setPubTime(Date pubTime) {
        this.pubTime = pubTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getBookCircleId() {
        return bookCircleId;
    }

    public void setBookCircleId(Integer bookCircleId) {
        this.bookCircleId = bookCircleId;
    }

    public UserInf getUserInf() {
        return userInf;
    }

    public void setUserInf(UserInf userInf) {
        this.userInf = userInf;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}