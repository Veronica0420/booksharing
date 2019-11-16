package com.ecust.sharebook.pojo.util.shelf;

import java.util.ArrayList;
import java.util.List;

public class CatgBook {
    private Integer catgId;
    private List<myShelf> catg_book_list;

    public CatgBook(){
        this.catg_book_list = new ArrayList<myShelf>();
    }

    public Integer getCatgId() {
        return catgId;
    }

    public void setCatgId(Integer catgId) {
        this.catgId = catgId;
    }

    public   List<myShelf> getCatg_book_list(){
        return catg_book_list;
    }
    public  void setCatg_book_list(List<myShelf> catg_book_list){
        this.catg_book_list = catg_book_list;
    }
}
