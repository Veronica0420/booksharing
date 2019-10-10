package com.ecust.sharebook.pojo;

public class AgreeInf {
    private Integer agreeId;

    private Integer postId;

    private Integer userId;

    private Boolean cancelAgree;

    public Integer getAgreeId() {
        return agreeId;
    }

    public void setAgreeId(Integer agreeId) {
        this.agreeId = agreeId;
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

    public Boolean getCancelAgree() {
        return cancelAgree;
    }

    public void setCancelAgree(Boolean cancelAgree) {
        this.cancelAgree = cancelAgree;
    }
}