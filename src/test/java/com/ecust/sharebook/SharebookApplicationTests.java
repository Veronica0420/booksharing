package com.ecust.sharebook;

import com.ecust.sharebook.mapper.FriendInfMapper;
import com.ecust.sharebook.mapper.rBcircleMemberMapper;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import com.ecust.sharebook.pojo.util.circle.shelfByCatg;
import com.ecust.sharebook.pojo.util.shelf.book;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    @Autowired
    private TUserBookService tUserBookService;

    @Autowired
    private TMessageService tMessageService;

    @Autowired
    private TBookUserBorrowService tBookUserBorrowService;
    @Autowired
    private optService toptService;

    @Autowired
    private TFriendService tFriendService;









    @Test
    public void contextLoads() throws ParseException {
        R r = new R();
        MessageInf messageInf = new MessageInf();
        messageInf.setSenderId(23);
        messageInf.setReceiverId(29);
        messageInf.setContent("测试");
        messageInf.setmType(2);

       int i = tMessageService.insertSelective(messageInf);
        int j = tMessageService.insert(messageInf);

       System.out.println(i+""+j);




    }


    @Test
    public void pass(){


    }



}