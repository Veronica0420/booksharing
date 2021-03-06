package com.ecust.sharebook.service.impl;


import com.ecust.sharebook.mapper.UserInfMapper;
import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.service.TMemberService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TMemberServiceImpl implements TMemberService {
	@Autowired
	private UserInfMapper userInfMapper;
	@Override
	public UserInf get(int id){
		return userInfMapper.get(id);
	}


	@Override
	public int save(UserInf tMember){
		return userInfMapper.save(tMember);
	}


	@Override
	public UserInf selectOne(Map<String, Object> params) {
		UserInf list = userInfMapper.selectUserByOepnid(params);
		if (list!=null ){
			return  list;
		}
		return null;
	}



	@Override
	public int updateByPrimaryKeySelective(UserInf record) {
		return userInfMapper.updateByPrimaryKeySelective( record);
	}

	@Override
	public UserInf selectByPrimaryKey(Integer userId) {
		return userInfMapper.selectByPrimaryKey( userId);
	}

	@Override
	public int updateSkeyByOpid(Map<String, Object> map) {
		return userInfMapper.updateSkeyByOpid(map);
	}

	@Override
	public List<UserInf> list(Map<String, Object> map) {
		List<UserInf> list = userInfMapper.list(map);
		if(list!=null && list.size()!=0){
			return  list;
		}
		return  null;
	}

	@Override
	public List<UserInf> listPublic(Map<String, Object> map) {

		List<UserInf> list = userInfMapper.listPublic(map);
		if(list!=null && list.size()!=0){
			return  list;
		}
		return  null;
	}


}
