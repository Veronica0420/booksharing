package com.ecust.sharebook.pojo;

public class FriendInf {
    private Integer fdriendId;

    private Integer uid;

    private Integer fid;

    private String alias;

    public Integer getFdriendId() {
        return fdriendId;
    }

    public void setFdriendId(Integer fdriendId) {
        this.fdriendId = fdriendId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }
}