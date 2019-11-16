package com.ecust.sharebook.service.impl;

import com.ecust.sharebook.mapper.CategoryInfMapper;
import com.ecust.sharebook.pojo.CategoryInf;
import com.ecust.sharebook.service.TCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TCataServiceIml implements TCateService {

    @Autowired
    private CategoryInfMapper tCategoryInfMapper;

    @Override
    public CategoryInf get(Long id) {
            return tCategoryInfMapper.get(id);
    }

    @Override
    public List<CategoryInf> list(){
            return tCategoryInfMapper.list();
    }

    @Override
    public CategoryInf getById(Integer catgId) {
        return tCategoryInfMapper.selectByPrimaryKey(catgId);
    }


}
