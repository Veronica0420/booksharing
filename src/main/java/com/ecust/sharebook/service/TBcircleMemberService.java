package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.rBcircleMember;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import com.ecust.sharebook.pojo.util.shelf.book;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

public interface TBcircleMemberService {

    List<Map<String ,Object>> findShelf (Map<String ,Object> param);

    List<Map<String ,Object>>findCatgShelf(Map<String ,Object> param);

    List<circleCatgBook> findCatg(List<Map<String ,Object>> list);

    List<rBcircleMember> list(Map<String, Object> map);

    int insert(rBcircleMember record);
    int insertmap(Map<String, Object> map);

    int insertSelective(rBcircleMember record);

    List<book> findShelfCircle (Map<String, Object> map);

    List<Map<String , Object>>findCircleBookList(Map<String, Object> map);

    int deleteByPrimaryKey(Integer bcMemId);


    rBcircleMember selectByPrimaryKey(Integer bcMemId);

    List<Integer> selectByMemberId(Map<String, Object> map);

    int updateByPrimaryKeySelective(rBcircleMember record);

    int updateByPrimaryKey(rBcircleMember record);

    int deletelist(List<Integer> list);

    int delete (Map<String, Object> map);


}
