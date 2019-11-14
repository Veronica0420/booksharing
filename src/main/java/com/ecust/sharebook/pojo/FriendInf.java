package com.ecust.sharebook.pojo;

import com.ecust.sharebook.utils.common.PinyinUtil;

import java.util.Date;

public class FriendInf {
    private Integer friendId;

    private Integer uid;

    private Integer fid;

    private String aliasu;

    private String aliasf;

    private Integer optType;  //0 申请 1 同意 2拒绝

    private Date addTime;

    private String avatarUrl;

    private String nickName;


    private String name;

    private Integer id;

    private Integer mid;

    private Boolean utf; //主动添加 true， 被动，false


    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
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

    public String getAliasu() {
        return aliasu;
    }

    public void setAliasu(String aliasu) {
        this.aliasu = aliasu;
    }

    public String getAliasf() {
        return aliasf;
    }

    public void setAliasf(String aliasf) {
        this.aliasf = aliasf;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Boolean getUtf() {
        return utf;
    }

    public void setUtf(Boolean utf) {
        this.utf = utf;
    }
}


