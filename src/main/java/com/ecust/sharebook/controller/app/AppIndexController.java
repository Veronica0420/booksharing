package com.ecust.sharebook.controller.app;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.BDself;
import com.ecust.sharebook.pojo.util.CatgBook;
import com.ecust.sharebook.pojo.util.IndexData;
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
        System.out.println("/myshelf");
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
            openId=jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
          if(openId != null){
              Map<String, Object> param = new HashMap<>();
              param.put("openId",openId);
              UserInf seMember = tMemberService.selectOne(param);
              param.clear();
              if(seMember!=null){
                  param.put("ownerId",seMember.getUserId());
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
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    @ResponseBody
    @GetMapping({"/asecond/bdself"})
    public R queryBook(@RequestParam Map<String, Object> params){
        R r=new R();
        System.out.println("/asecond/bdself");
        try {
            String isbn = new String();
            BDself bDself = new BDself();
            Map<String, Object> result = new HashMap<>();
            for(String key : params.keySet()){
                if(key.equals("isbn")){
                    isbn = params.get(key).toString();
                }
            }
            BookInf bk = tBookService.selectByIsbn(isbn);
            if(bk!=null){
                result.put("is_exist",1);
                rUserBook rub=tUserBookService.SelectByIsbn(params);
                List<rBookCategory> rbg = tBookCategoryService.findbyIsbn(params);//0,1...
                List<String> catg = new ArrayList<>();
                //根据目录id 找出目录name
                if(rbg!=null) {
                    for (int i = 0; i < rbg.size(); i++) {
                        rBookCategory cg = rbg.get(i);
                        catg.add(tCateService.getById(cg.getCatgId()).getCatgName());
                    }
                }
                else
                {
                    catg.add("无");

                }
                bDself.setCatg(catg);
                bDself.setIsbn(isbn);
                bDself.setBookName(bk.getBookName());
                bDself.setBriefIntro(bk.getBriefIntro());
                bDself.setPicPath(bk.getPicPath());
                bDself.setPrivacy(rub.getPrivacy());
                bDself.setBorrowState(rub.getBorrowState());
                r.put("data",bDself);
            }
            else{
                result.put("is_exist",0);
            }

            r.put("status",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    @ResponseBody
    @GetMapping({"/asecond/privacy"})
    public R setPrivacy(@RequestParam Map<String, Object> params){
        System.out.println("/asecond/privacy");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_sucess", -1);
            String openId = new String();
            String isbn = new String();
            String privacy = new String();
            openId=jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            isbn=params.get("isbn").toString();
            privacy=params.get("privacy").toString();
            System.out.println(openId+':'+isbn+":"+privacy);
            params.put("openId",openId);

            if(openId!=null&&isbn!=null&&privacy!=null){
                UserInf seMember = tMemberService.selectOne(params);
                if(seMember!=null){
                    params.put("ownerId",seMember.getUserId());
                    int i = tUserBookService.updatePrivacy(params);
                    if(i==0) {
                        result.put("save_sucess", 0); //更新失败
                    }else{
                        result.put("save_sucess", 1); //更新成功
                    }
                }

            }
            r.put("data",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



    @ResponseBody
    @GetMapping("/scan/book")
    public R scanQueryBook(@RequestParam Map<String, Object> params){
          System.out.println("/scan/book");
        R r=new R();
        try {
            String isbn = new String();
            isbn=params.get("isbn").toString();

            BookInf bk = tBookService.selectByIsbn(isbn);
            Map<String, Object> result = new HashMap<>();
            if(bk!=null){
                result.put("is_exist",1);
                r.put("book",bk);
                r.put("status",result);
            }
            else{
                result.put("is_exist",0);
                r.put("status",result);
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



    @ResponseBody
    @RequestMapping("/login")
    public R login(HttpServletRequest req) throws Exception{
        System.out.println("/login");

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String code = req.getParameter("code");
        String mode = req.getParameter("mode");
        R r = new R();

        if (StringUtils.isBlank(code)) {
            System.out.println("code is empty");
            result.put("result", "-1");
            return r.put("data", result);
        }
        WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
        String openId = session.getOpenid();
        String sessionKey = session.getSessionKey();
        String unionId =session.getUnionid(); //null
        //jwt 加密 token
        UserInf jwtAccout = new UserInf();
        jwtAccout.setOpenId(openId);
        jwtAccout.setSessionKey(sessionKey);
        String token = jwtUtil.createTokenByWxAccount(jwtAccout);

        params.put("openId", openId);

        UserInf seMember = tMemberService.selectOne(params);
        if (seMember == null) {
            System.out.println("用户不存在");
            if(mode.equals("login")) {
                System.out.println("mode is login");
                //不存在就新建用户
                String userInfo = req.getParameter("userInfo");
                Map<String, Object> me = JSONUtils.jsonToMap(userInfo);
                seMember = new UserInf();
                if ("1".equals(me.get("gender").toString())) {
                    seMember.setGender(true);
                } else {
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
                int i = tMemberService.updateByPrimaryKeySelective(seMember);
                if(i==0) {
                    result.put("save_sucess", 0); //更新失败
                }else{
                    result.put("save_sucess", 1); //更新成功
                }
                result.put("token", token);
            }else{
                System.out.println("mode is tlogin");
            }

            result.put("result", 0);
        } else {
            System.out.println("用户存在");
            try {
                if(mode.equals("login")){
                    System.out.println("mode is login");
                }
                else{
                    params.put("sessionKey",sessionKey);
                    int i = tMemberService.updateSkeyByOpid(params);
                    if(i==0) {
                        result.put("save_sucess", 0); //更新失败
                    }else{
                        result.put("save_sucess", 1); //更新成功
                    }
                }
                result.put("token", token);
                result.put("result", 1);
            }catch (Exception e){
                e.printStackTrace();
                return R.error();
            }
        }
        r.put("data", result);
        return r;

    }

}
