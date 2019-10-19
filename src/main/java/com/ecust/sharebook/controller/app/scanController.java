package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.BookInf;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/small")
public class scanController {

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
     * scan/scResult 查询
     **/

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


    /**
     * scan/scResult 添加书籍到我的书架中
     **/

    @ResponseBody
    @GetMapping("scan/addShelf")
    public R addShelf(@RequestParam Map<String, Object> params){
        System.out.println("/scan/addShelf");
        R r=new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_sucess", -1);
            String openId = new String();
            String isbn = new String();
            String addTime = new String();

            openId = jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            isbn =params.get("isbn").toString();
            Integer privacy =  Integer.valueOf(params.get("privacy").toString());
            addTime = params.get("addTimes").toString();
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(addTime);
            params.put("addTime",date);
            System.out.println(params);
            params.put("openId",openId);
            if(openId!=null&&isbn!=null&&privacy!=null&&addTime!=null){
                UserInf seMember = tMemberService.selectOne(params);
                if(seMember!=null){
                    rUserBook rub = tUserBookService.SelectByIsbn(params);
                    if(rub==null){
                        //我的书架中无此书
                        result.put("status", 2);
                        params.put("ownerId",seMember.getUserId());
                        int i = tUserBookService.save(params);
                        if(i==0) {
                            result.put("save_success", 0); //更新失败
                        }else{
                            result.put("save_success", 1); //更新成功
                        }
                    }
                    else{
                        result.put("status", 1);
                    }
                }else{
                    result.put("status", 0);
                }

            }else{
                result.put("status", -1);
            }

            r.put("data",result);

        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


}
