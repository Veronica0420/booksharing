package com.ecust.sharebook.pojo.util.chat;

import com.ecust.sharebook.pojo.MessageInf;

import java.util.List;

public class messageBookId {

    // 发送者信息
    private Integer userId;

    private String nickName;

    private String avatarUrl;

    private List<MessageInf> messageInfs; // 可选


    private String bookName;


    private String picPath;


    private   MessageInf  currentMessageInf;


    public messageBookId(Integer userId, String nickName, String avatarUrl, MessageInf  currentMessageInf){
        this.setAvatarUrl(avatarUrl);
        this.setCurrentMessageInf(currentMessageInf);
        this.setNickName(nickName);
        this.setUserId(userId);

    }

    public messageBookId(){

    }

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

    public List<MessageInf> getMessageInfs() {
        return messageInfs;
    }

    public void setMessageInfs(List<MessageInf> messageInfs) {
        this.messageInfs = messageInfs;
    }

    public MessageInf getCurrentMessageInf() {
        return currentMessageInf;
    }

    public void setCurrentMessageInf(MessageInf currentMessageInf) {
        this.currentMessageInf = currentMessageInf;
    }
}
