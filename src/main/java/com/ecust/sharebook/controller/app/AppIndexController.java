package com.ecust.sharebook.controller.app;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import com.ecust.sharebook.utils.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/small")
public class AppIndexController {

    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private TCateService tCateService;
    @Autowired
    private TUserBookService tUserBookService;
    @Autowired
    private TBookService tBookService;
    @Autowired
    private TBookCategoryService tBookCategoryService;
    @Autowired
    private WxMaService wxService;
    @Autowired
    private JwtUtil jwtUtil;



    @ResponseBody
    @GetMapping("/myshelf")
    public R index(@RequestParam Map<String, Object> params){
        System.out.println("myshelf");
        IndexData data = new IndexData();
        R r=new R();
        try {
            //查询目录
            List<CategoryInf> cat_list  = tCateService.list();;
            List<CatgBook> catg_book_list = new ArrayList<>();

            for(CategoryInf ci:cat_list){
                CatgBook cb = new CatgBook();
                cb.setCatgId(ci.getCatgId());
                catg_book_list.add(cb);
            }

            CatgBook cb = new CatgBook();
            cb.setCatgId(0); //所有
            catg_book_list.add(cb);


            //查询所有书籍
            String openId = new String();
            for(String key : params.keySet()){
                if(key.equals("access_token")){
                   /// openId = params.get(key).toString();
                    openId = jwtUtil.getWxOpenIdByToken(params.get(key).toString());
                }
            }
          if(openId != null){
              Map<String, Object> param = new HashMap<>();
              param.put("openId",openId);
              UserInf seMember = tMemberService.selectOne(param);
              param.clear();
              if(seMember!=null){
                  param.put("ownerId",seMember.getUserId());
                  System.out.println(param);
                  List<String> isbn = tUserBookService.selectISBNbyID(param);
                  if(isbn != null){
                     for(String is :isbn){
                         BookInf bk = tBookService.selectByIsbn(is);
                         for(CatgBook cbk :catg_book_list){
                             if(cbk.getCatgId()==0){
                                 cbk.getCatg_book_list().add(bk);
                                 continue;
                             }
                             param.clear();
                             param.put("isbn",is);
                             param.put("catgId",cbk.getCatgId());

                             rBookCategory rbg = tBookCategoryService.findCatgbyIsbn(param);
                             if(rbg !=null){
                                 cbk.getCatg_book_list().add(bk);
                             }
                         }
                     }
                  }


              }

          }


          data.setCat_list(cat_list);
          data.setCatg_book_list(catg_book_list);

            r.put("data",data);
            System.out.println(data);
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    @ResponseBody
    @GetMapping({"/scan/book" ,"/asecond/bdetail"})
    public R queryBook(@RequestParam Map<String, Object> params){
      //  System.out.println("params;;;;"+params);
        R r=new R();
        try {
            String isbn = new String();
            for(String key : params.keySet()){
                if(key.equals("isbn")){
                    isbn = params.get(key).toString();
                }
            }
            BookInf bk = tBookService.selectByIsbn(isbn);
            
            r.put("data",bk);
            System.out.println(bk);
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


    @ResponseBody
    @RequestMapping("/login")
    public R login(HttpServletRequest req)throws Exception{
        System.out.println("login");

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String code = req.getParameter("code");
     //   String signature = req.getParameter("signature");
     //   String rawData = req.getParameter("userInfo");
      //  String encryptedData = req.getParameter("encrypted_data");
        //String iv  = req.getParameter("iv");
        R r=new R();

        if (StringUtils.isBlank(code)) {
            System.out.println("code is empty");
            result.put("errcode","0");
            return  r.put("data",result);
        }
        WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
        String openId =session.getOpenid();
        String unionId =session.getUnionid();
        String sessionKey =session.getSessionKey();
        System.out.println("uuid"+unionId);
        params.put("openId",openId);
        UserInf seMember = tMemberService.selectOne(params);
        if (seMember == null){
            System.out.println("用户不存在");

            //不存在就新建用户
            String userInfo = req.getParameter("userInfo");
            Map<String,Object> me = JSONUtils.jsonToMap(userInfo);
            seMember =new UserInf();
            if ("1".equals(me.get("gender").toString())){
                seMember.setGender(true);
            }else{
                seMember.setGender(false);
            }
            seMember.setOpenId(openId);
            seMember.setUnionId(unionId);
            seMember.setSessionKey(sessionKey);
            seMember.setCountry(me.get("country").toString());
            seMember.setProvince(me.get("province").toString());
            seMember.setCity(me.get("city").toString());
            seMember.setNickName(me.get("nickName").toString());
            seMember.setTel(null);


            try {
                int i =tMemberService.save(seMember);
                //5 . JWT 返回自定义登陆态 Token
                String token = jwtUtil.createTokenByWxAccount(seMember);
                System.out.println("token" +token+"unnid"+unionId);
                result.put("token", token);
                r.put("data",result);

            }catch (Exception e){
                e.printStackTrace();
                return R.error();
            }
        }else{
            System.out.println("用户存在");
            //更新用户sessinkey
            try {
                int i = tMemberService.updateByPrimaryKeySelective(seMember);
                String token = jwtUtil.createTokenByWxAccount(seMember);
                result.put("token", token);

            }catch (Exception e){
                e.printStackTrace();
                return R.error();
            }

            System.out.println("授权");
            r.put("data",result);
        }



        return r;
    }



    @ResponseBody
    @RequestMapping("/tlogin")
    public R tlogin(HttpServletRequest req) throws Exception{
        System.out.println("tlogin");

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String code = req.getParameter("code");
        //   String signature = req.getParameter("signature");
        //   String rawData = req.getParameter("userInfo");
        //  String encryptedData = req.getParameter("encrypted_data");
        //String iv  = req.getParameter("iv");
        R r = new R();

        if (StringUtils.isBlank(code)) {
            System.out.println("code is empty");
            result.put("errcode", "0");
            return r.put("data", result);
        }
        WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);


        String openId = session.getOpenid();
        String unionId = session.getUnionid();
        String sessionKey = session.getSessionKey();
        System.out.println("uuid" + unionId);
        params.put("openId", openId);
        UserInf seMember = tMemberService.selectOne(params);
        if (seMember == null) {
            System.out.println("用户不存在");
            result.put("result", 0);

            r.put("data", result);

        } else {
            System.out.println("用户存在");
            try {
                String token = jwtUtil.createTokenByWxAccount(seMember);
                result.put("token", token);
                //更新用户sessinkey
                result.put("result", 1);
                r.put("data", result);
            }catch (Exception e){
                e.printStackTrace();
                return R.error();
            }


        }
        return r;


    }


}
