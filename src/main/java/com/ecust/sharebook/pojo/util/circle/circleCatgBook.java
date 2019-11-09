package com.ecust.sharebook.pojo.util.circle;

import java.util.List;

public class circleCatgBook {
    private Integer catgId;

    private String catgName;

    private List<shelfByCatg> catg_book_list;

    private int count;

    public Integer getCatgId() {
        return catgId;
    }

    public void setCatgId(Integer catgId) {
        this.catgId = catgId;
    }

    public List<shelfByCatg> getCatg_book_list() {
        return catg_book_list;
    }

    public void setCatg_book_list(List<shelfByCatg> catg_book_list) {
        this.catg_book_list = catg_book_list;
    }

    public String getCatgName() {
        return catgName;
    }

    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public  void setAll(Integer catgId,String catgName ){
        this.setCount(0);
        this.setCatgName(catgName);
        this.setCatgId(catgId);

    }

    public  void setAll0(Integer catgId,String catgName,int count ){


        this.setCount(count);
        this.setCatgName(catgName);
        this.setCatgId(catgId);

    }
}
