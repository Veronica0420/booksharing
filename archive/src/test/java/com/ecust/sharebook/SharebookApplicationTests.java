package com.ecust.sharebook;

import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import com.ecust.sharebook.pojo.util.circle.shelfByCatg;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SharebookApplicationTests {


    @Autowired
    private TBcircleMemberService tBcircleMemberService;
    @Autowired
    private TCateService tCateService;
    @Autowired
    private TBookService tBookService;
    @Autowired
    private TPostInfService tPostInfService;
    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private TCommentInfService tCommentInfService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TBookCircleService tBookCircleService;




    @Test
    public void contextLoads() throws ParseException {
        R r = new R();
       Map<String, Object> result = new HashMap<>();
        Map<String, Object> param = new HashMap<>();



        Integer bookCircleId = Integer.valueOf(2);
        //String isbn = "9787121355950";
        param.put("bookCircleId", bookCircleId);

        Integer userId = Integer.valueOf(23);
        param.put("userId", userId);;

        UserInf me = tMemberService.list(param).get(0); //查询用户信息（根据openId 查询 userId）
        rBcircleMember rBcircleMemberTemp = new rBcircleMember();
        rBcircleMemberTemp.setBookCircleId(bookCircleId);
        rBcircleMemberTemp.setMemberId(me.getUserId());

        int i  = tBcircleMemberService.insert(rBcircleMemberTemp);
        if(i==1){
            result.put("is_exist", 1);
        }else
            result.put("is_exist", 0);


        r.put("status", result);


        System.out.println(r);





    }



}