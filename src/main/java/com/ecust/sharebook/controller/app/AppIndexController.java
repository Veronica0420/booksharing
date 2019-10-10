package com.ecust.sharebook.controller.app;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.JSONUtils;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import com.ecust.sharebook.utils.R;
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


    @ResponseBody
    @RequestMapping("/login")
    public R login(HttpServletRequest req)throws Exception{
        R r=new R();
        String code = req.getParameter("code");
        if (StringUtils.isBlank(code)) {
            System.out.println("code is empty");
        }
        WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
        Map<String, Object> params = new HashMap<>();
        params.put("openId",session.getOpenid());
        UserInf seMember = tMemberService.selectOne(params);
       // System.out.println(params+""+seMember.getOpenId());


        if (seMember == null){
            System.out.println("未授权");
//			System.out.println(session.getSessionKey());
//			System.out.println(session.getOpenid());
//			System.out.println(session.getExpiresin().toString());
//			System.out.println(JsonUtils.toJson(session));
            String userInfo = req.getParameter("userInfo");
            Map<String,Object> me = JSONUtils.jsonToMap(userInfo);
            //   System.out.println(userInfo);

            UserInf m =new UserInf();
            if ("1".equals(me.get("gender").toString())){
                m.setGender(true);
            }else{
                m.setGender(false);
            }
            m.setOpenId(session.getOpenid());
            m.setCountry(me.get("country").toString());
            m.setProvince(me.get("province").toString());
            m.setUnionId(session.getUnionid());
            m.setCity(me.get("city").toString());
            m.setSessionKey(session.getSessionKey());
            m.setNickName(me.get("nickName").toString());
            m.setTel(null);


            try {
                int i =tMemberService.save(m);
                r.put("data",tMemberService.get(m.getUserId().intValue()));
            }catch (Exception e){
                e.printStackTrace();
                return R.error();
            }
        }else{
            System.out.println("授权");
            r.put("data",seMember);
        }



        return r;
    }


    @ResponseBody
    @GetMapping("/myshelf")
    public R index(@RequestParam Map<String, Object> params){
        System.out.println(params);
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
                    openId = params.get(key).toString();
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



}
