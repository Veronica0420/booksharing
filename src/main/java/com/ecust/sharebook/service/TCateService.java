package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.CategoryInf;

import java.util.List;

public interface TCateService {

    CategoryInf get(Long id);

    List<CategoryInf> list();

    CategoryInf getById(Integer catgId);


}
