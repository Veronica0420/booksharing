package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.service.TBookCircleService;
import com.ecust.sharebook.service.TMemberService;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * written by Sijar
 */
@Controller
@RequestMapping("/small")
public class BookCircleController {
    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private TBookCircleService tBookCircleService;
    @Autowired
    private JwtUtil jwtUtil;

    @ResponseBody
    @GetMapping("/bookCircle")
    public R myBookCircle(@RequestParam Map<String,Object> params){
        System.out.println("bookCircle");
        List<BookCircleInf> my_book_circle_list = new ArrayList<>();
        R r=new R();
        try {
            String openId = new String();
            for(String key : params.keySet()){
                if(key.equals("access_token")){
                    /// openId = params.get(key).toString();
                    openId = jwtUtil.getWxOpenIdByToken(params.get(key).toString());
                }
            }
            if(openId != null) {
                Map<String, Object> param = new HashMap<>();
                param.put("openId", openId);
                UserInf seMember = tMemberService.selectOne(param);
                param.clear();
                if (seMember != null) {
                    param.put("createrId", seMember.getUserId());
                    System.out.println("USER_ID="+param);
                    //查询所有我创建的图书圈
                    my_book_circle_list = tBookCircleService.selectbyCreaterID(param);
                    param.clear();
                }
            }
            r.put("my_book_circle_list",my_book_circle_list);
            System.out.println("my_book_circle_list:"+my_book_circle_list);
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }
}
