package com.ecust.sharebook.pojo.util.chat;

import com.ecust.sharebook.pojo.MessageInf;

import java.util.List;

public class messageBookId {

    // 发送者信息
    private Integer userId;

    private String nickName;

    private String avatarUrl;



    private MessageInf messageInfs;






    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public  void setOther(Integer userId,String nickName,String avatarUrl, MessageInf messageInfs){
        this.setUserId(userId);
        this.setNickName(nickName);
        this.setAvatarUrl(avatarUrl);
        this.setMessageInfs(messageInfs);

    }



    public MessageInf getMessageInfs() {
        return messageInfs;
    }

    public void setMessageInfs(MessageInf messageInfs) {
        this.messageInfs = messageInfs;
    }


}
