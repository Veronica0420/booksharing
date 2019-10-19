package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.BDself;
import com.ecust.sharebook.pojo.util.CatgBook;
import com.ecust.sharebook.pojo.util.IndexData;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/small")
public class shelfController {
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
    private JwtUtil jwtUtil;


    /**
     * shelf  加载书架
     **/

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
                List<  Map<String, Object> > uBook = new ArrayList<>();
                param.put("openId",openId);
                UserInf seMember = tMemberService.selectOne(param);
                param.clear();
                if(seMember!=null){
                    param.put("ownerId",seMember.getUserId());
                    uBook = tUserBookService.selectByOwId(param);
                    if(uBook != null){
                        for(Map<String, Object> is :uBook){
                            String isbn= is.get("ISBN").toString();
                            Date addTime = (Date) is.get("ADD_TIME");

                            BookInf bk = tBookService.selectByIsbn(is.get("ISBN").toString());
                            BDself bDself = new BDself();
                            bDself.setIsbn(bk.getIsbn());
                            bDself.setBookName(bk.getBookName());
                            bDself.setPicPath(bk.getPicPath());
                            bDself.setAddTime(addTime);
                            for(CatgBook cbk :catg_book_list){
                                if(cbk.getCatgId()==0){
                                    cbk.getCatg_book_list().add(bDself);
                                    continue;
                                }
                                param.clear();
                                param.put("isbn",isbn);
                                param.put("catgId",cbk.getCatgId());
                                rBookCategory rbg = tBookCategoryService.findCatgbyIsbn(param);
                                if(rbg !=null){
                                    cbk.getCatg_book_list().add(bDself);
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

    /**
     * asecond/bdself 详情界面
     **/
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

    /**
     * asecond/bdself  设置公开/私密
     **/

    @ResponseBody
    @GetMapping({"/asecond/privacy"})
    public R setPrivacy(@RequestParam Map<String, Object> params){
        System.out.println("/asecond/privacy");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_success", -1);
            String openId = new String();
            String isbn = new String();
            openId=jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            isbn=params.get("isbn").toString();
            Integer privacy =  Integer.valueOf(params.get("privacy").toString());
            System.out.println(openId+':'+isbn+":"+privacy);
            params.put("openId",openId);

            if(openId!=null&&isbn!=null&&privacy!=null){
                UserInf seMember = tMemberService.selectOne(params);
                if(seMember!=null){
                    params.put("ownerId",seMember.getUserId());
                    int i = tUserBookService.updatePrivacy(params);
                    if(i==0) {
                        result.put("save_success", 0); //更新失败
                    }else{
                        result.put("save_success", 1); //更新成功
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

}
