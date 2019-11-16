package com.ecust.sharebook.pojo.util.friend;

import com.ecust.sharebook.utils.common.PinyinUtil;

public class friendList {

    private Integer fid;

    private String alias;

    private String avatarUrl;

    private String nickName;

    private String name;

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
        this.alias = alias;
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


    public String getFirstChar(){
        String name = null;
        if(this.getAlias()!= null && this.getAlias().length()!=0){

            name = this.getAlias();
        }else
            name = this.getNickName();

        this.setName(name);
        return   PinyinUtil.getFirstLetter(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
