package com.ecust.sharebook.pojo;

public class rBcircleMember {
    private Integer bcMemId;

    private Integer bookCircleId;

    private Integer memberId;

    private Integer ifCreater;

    private  UserInf userInf;

    private  int friend; //身份标识 0 自己 1 好友-已申请 2-好友-同意 3不是好友

    public Integer getBcMemId() {
        return bcMemId;
    }

    public void setBcMemId(Integer bcMemId) {
        this.bcMemId = bcMemId;
    }

    public Integer getBookCircleId() {
        return bookCircleId;
    }

    public void setBookCircleId(Integer bookCircleId) {
        this.bookCircleId = bookCircleId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public UserInf getUserInf() {
        return userInf;
    }

    public void setUserInf(UserInf userInf) {
        this.userInf = userInf;
    }

    public Integer getIfCreater() {
        return ifCreater;
    }

    public void setIfCreater(Integer ifCreater) {
        this.ifCreater = ifCreater;
    }


    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }
}