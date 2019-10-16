package com.ecust.sharebook.pojo.util;

import com.ecust.sharebook.pojo.CategoryInf;
import com.ecust.sharebook.pojo.util.CatgBook;

import java.util.ArrayList;
import java.util.List;

public class IndexData {
    private List<CategoryInf> cat_list ;
    private List<CatgBook> catg_book_list;

public IndexData(){
    cat_list = new ArrayList<CategoryInf>();
    catg_book_list = new ArrayList<CatgBook>();
}

    public List<CategoryInf> getCat_list() {
        return cat_list;
    }

    public void setCat_list(List<CategoryInf> cat_list) {
        this.cat_list = cat_list;
    }

    public List<CatgBook> getCatg_book_list() {
        return catg_book_list;
    }

    public void setCatg_book_list(List<CatgBook> catg_book_list) {
        this.catg_book_list = catg_book_list;
    }
}
