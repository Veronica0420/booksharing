package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.rBcircleMember;

public interface rBcircleMemberMapper {
    int deleteByPrimaryKey(Integer bcMemId);

    int insert(rBcircleMember record);

    int insertSelective(rBcircleMember record);

    rBcircleMember selectByPrimaryKey(Integer bcMemId);

    int updateByPrimaryKeySelective(rBcircleMember record);

    int updateByPrimaryKey(rBcircleMember record);
}