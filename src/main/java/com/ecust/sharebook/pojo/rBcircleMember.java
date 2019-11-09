package com.ecust.sharebook.pojo;

public class rBcircleMember {
    private Integer bcMemId;

    private Integer bookCircleId;

    private Integer memberId;

    private Integer ifCreater;

    private  UserInf userInf;

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

    public Integer getIfCreater() {
        return ifCreater;
    }

    public void setIfCreater(Integer ifCreater) {
        this.ifCreater = ifCreater;
    }

    public UserInf getUserInf() {
        return userInf;
    }

    public void setUserInf(UserInf userInf) {
        this.userInf = userInf;
    }
}