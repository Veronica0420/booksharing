package com.ecust.sharebook.pojo;

import java.util.Date;

public class CommentInf {
    private Integer commentId;

    private Integer postId;

    private Integer userId;

    private String content;

    private Date comtTime;

    private String comtTimeS;

    private  UserInf userInf;

    private  int flag; //0 自己 1 陌生人

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getComtTime() {
        return comtTime;
    }

    public void setComtTime(Date comtTime) {
        this.comtTime = comtTime;
    }

    public UserInf getUserInf() {
        return userInf;
    }

    public void setUserInf(UserInf userInf) {
        this.userInf = userInf;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getComtTimeS() {
        return comtTimeS;
    }

    public void setComtTimeS(String comtTimeS) {
        this.comtTimeS = comtTimeS;
    }
}