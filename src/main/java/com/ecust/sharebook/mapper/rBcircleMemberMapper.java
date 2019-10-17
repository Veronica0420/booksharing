package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.rBcircleMember;

import java.util.List;
import java.util.Map;

public interface rBcircleMemberMapper {
    int deleteByPrimaryKey(Integer bcMemId);

    int insert(rBcircleMember record);

    int insertSelective(rBcircleMember record);

    rBcircleMember selectByPrimaryKey(Integer bcMemId);

    List<Integer> selectByMemberId(Map<String, Object> map);
    int updateByPrimaryKeySelective(rBcircleMember record);

    int updateByPrimaryKey(rBcircleMember record);
}