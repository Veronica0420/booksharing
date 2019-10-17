package com.ecust.sharebook.pojo.util;

import com.ecust.sharebook.pojo.BookInf;
import org.springframework.context.annotation.Bean;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

public class CatgBook {
    private Integer catgId;
    private List<BDself> catg_book_list;

    public CatgBook(){
        this.catg_book_list = new ArrayList<BDself>();
    }

    public Integer getCatgId() {
        return catgId;
    }

    public void setCatgId(Integer catgId) {
        this.catgId = catgId;
    }

    public   List<BDself> getCatg_book_list(){
        return catg_book_list;
    }
    public  void setCatg_book_list(List<BDself> catg_book_list){
        this.catg_book_list = catg_book_list;
    }
}
