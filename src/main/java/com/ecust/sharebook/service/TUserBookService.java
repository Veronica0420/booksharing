package com.ecust.sharebook.service;


import com.ecust.sharebook.pojo.rUserBook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TUserBookService {

    List<Map<String, Object>>  selectByOwId(Map<String, Object> params);

    rUserBook SelectByIsbn(Map<String, Object> map);

    int updatePrivacy(Map<String, Object> map);

    int save(Map<String, Object> map);
}
