package com.ecust.sharebook.pojo;

public class BookCircleInf {
    private Integer bookCircleId;

    private String bcName;

    private Integer createrId;

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