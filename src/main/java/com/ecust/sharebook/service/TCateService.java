package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.CategoryInf;
import com.ecust.sharebook.pojo.CatgBook;

import java.util.List;
import java.util.Map;

public interface TCateService {

    CategoryInf get(Long id);

    List<CategoryInf> list();

}
