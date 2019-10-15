package com.ecust.sharebook.service;


import com.ecust.sharebook.pojo.UserInf;

import java.util.List;
import java.util.Map;


public interface TMemberService {

	UserInf get(int id);

	int save(UserInf tMember);


	UserInf selectOne(Map<String, Object> params);

	public UserInf login(Map<String, Object> params);

	int updateByPrimaryKeySelective(UserInf record);
}
