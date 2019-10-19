package com.ecust.sharebook.controller.app;

import com.alibaba.fastjson.parser.SymbolTable;
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
import java.text.SimpleDateFormat;
import java.util.*;


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
        System.out.println("<----bookCircle       start--->");
        List<BookCircleInf> my_book_circle_list = new ArrayList<>();
        List<BookCircleInf> other_book_circle_list = new ArrayList<>();
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
                    System.out.println("----------USER_ID="+param);
                    //查询所有我创建的图书圈
                    my_book_circle_list = tBookCircleService.selectbyCreaterID(param);
                    //查询所有我未加入的图书圈
                    other_book_circle_list=tBookCircleService.selectbyNotCreaterIDMemberID(param);
                    param.clear();
                }
            }
            r.put("my_book_circle_list",my_book_circle_list);
            r.put("other_book_circle_list",other_book_circle_list);
            System.out.println("--------my_book_circle_list:"+my_book_circle_list);
            System.out.println("--------other_book_circle_list:"+other_book_circle_list);
            System.out.println("<-------bookCircle  -------end>");

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    @ResponseBody
    @GetMapping("/newBookCircle")
    public R newBookCircle(@RequestParam Map<String,Object> params){
        System.out.println("<--newBookCircle  start----->");
        System.out.println("params:"+params);
        String bcName=new String();
        String intro=new String();
        Date establishTime=new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        R r=new R();
        try {
            String openId = new String();
            openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            bcName=params.get("bcName").toString();
            intro=params.get("intro").toString();
            if(openId != null) {
                Map<String, Object> param = new HashMap<>();
                param.put("openId", openId);
                UserInf seMember = tMemberService.selectOne(param);
                param.clear();
                if (seMember != null) {
                    param.put("createrId", seMember.getUserId());
                    param.put("bcName",bcName);
                    param.put("intro",intro);
                    param.put("establishTime",dateFormat.format(establishTime));
                    System.out.println("param::::::"+param);
                    r.put("result",tBookCircleService.insert(param));
                    param.clear();
                }
            }


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }
}
