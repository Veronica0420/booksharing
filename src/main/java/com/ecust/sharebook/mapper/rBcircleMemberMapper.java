package com.ecust.sharebook.mapper;

import com.ecust.sharebook.pojo.CommentInf;
import com.ecust.sharebook.pojo.rBcircleMember;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import org.apache.ibatis.annotations.Param;

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


    List<Map<String ,Object>> findShelf (Map<String ,Object> param);

    List<circleCatgBook> findCatg(@Param("list")List<Map<String ,Object>> list);


    List<Map<String ,Object>>findCatgShelf(Map<String ,Object> param);

    List<rBcircleMember> list(Map<String, Object> map);
}