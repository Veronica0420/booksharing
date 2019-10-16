package com.ecust.sharebook.service;


import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rUserBook;

import java.util.List;
import java.util.Map;


public interface TMemberService {

	UserInf get(int id);

	int save(UserInf tMember);


	UserInf selectOne(Map<String, Object> params);


	int updateByPrimaryKeySelective(UserInf record);

	int updateSkeyByOpid(Map<String, Object> map);


}
