package com.ecust.sharebook.service;

import com.ecust.sharebook.pojo.rBcircleMember;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import org.apache.ibatis.annotations.Param;

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


}
