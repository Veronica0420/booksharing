package com.ecust.sharebook.service;


import com.ecust.sharebook.pojo.rUserBook;

import java.util.List;
import java.util.Map;

public interface TUserBookService {

    List<String> selectISBNbyID(Map<String, Object> params);
}
