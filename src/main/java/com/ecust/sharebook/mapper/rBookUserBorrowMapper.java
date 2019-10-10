package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.rBookUserBorrow;

public interface rBookUserBorrowMapper {
    int deleteByPrimaryKey(Integer borrowId);

    int insert(rBookUserBorrow record);

    int insertSelective(rBookUserBorrow record);

    rBookUserBorrow selectByPrimaryKey(Integer borrowId);

    int updateByPrimaryKeySelective(rBookUserBorrow record);

    int updateByPrimaryKey(rBookUserBorrow record);
}