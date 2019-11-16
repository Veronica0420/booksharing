package com.ecust.sharebook.pojo;

import java.util.Date;

public class BookCircleInf {
    private Integer bookCircleId;

    private String bcName;

    private Integer createrId;

    private String circlePicPath;
    private Date establishTime;

    private String intro;

    public String getCirclePicPath() {
        return circlePicPath;
    }

    public void setCirclePicPath(String circlePicPath) {
        this.circlePicPath = circlePicPath;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getBookCircleId() {
        return bookCircleId;
    }

    public void setBookCircleId(Integer bookCircleId) {
        this.bookCircleId = bookCircleId;
    }

    public String getBcName() {
        return bcName;
    }

    public void setBcName(String bcName) {
        this.bcName = bcName == null ? null : bcName.trim();
    }

    public Integer getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Integer createrId) {
        this.createrId = createrId;
    }
}