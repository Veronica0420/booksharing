package com.ecust.sharebook.pojo;

public class rBookCategory {
    private Integer bCatgId;

    private String isbn;

    private Integer catgId;



    public Integer getbCatgId() {
        return bCatgId;
    }

    public void setbCatgId(Integer bCatgId) {
        this.bCatgId = bCatgId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public Integer getCatgId() {
        return catgId;
    }

    public void setCatgId(Integer catgId) {
        this.catgId = catgId;
    }

}