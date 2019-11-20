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

import java.text.ParseException;
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
     * param: isbn
     * res:bookInf{
     * 包括：List<CategoryInf>)
     *      ifnull(bookId -1)
     *      ifnull(catgId -1)
     * }
     *
     *       s_exist:0,1
     *       count
     **/

    @ResponseBody
    @GetMapping("/scan/book")
    public R scanQueryBook(@RequestParam Map<String, Object> params) {
        System.out.println("/scan/book");
        R r = new R();
        Map<String, Object> result = new HashMap<>();

        try {
            BookInf bk = tBookService.ScanResult(params).get(0);


            if (bk != null) {
                int count = tBookService.ScanResult(params).size();
                r.put("book", bk);
                r.put("count",count);
                result.put("is_exist", 1);
            } else {
                result.put("is_exist", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0);

        }

        r.put("status", result);
        return r;
    }


    /**
     * scan/scResult 添加书籍到我的书架中
     * params: isbn
     *         addTime
     *         openId
     *         private
     * res: status:save_success 0 1
     **/

    @ResponseBody
    @GetMapping("scan/addShelf")
    public R addShelf(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/scan/addShelf");
        R r = new R();
        Map<String, Object> result = new HashMap<>();


        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        String isbn = params.get("isbn").toString();
        String taddTime = params.get("addTime").toString();
        Integer privacy = Integer.valueOf(params.get("privacy").toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date addTime = format.parse(taddTime);


        if (openId != null && isbn != null && privacy != null && addTime != null) {
            params.put("openId", openId);
            try {
                UserInf seMember = tMemberService.listPublic(params).get(0);
                if (seMember != null) {
                   rUserBook rUserBookTemp = new rUserBook();
                   rUserBookTemp.setAddTime(addTime);
                   rUserBookTemp.setIsbn(isbn);
                   rUserBookTemp.setOwnerId(seMember.getUserId());
                   rUserBookTemp.setPrivacy(privacy);
                    int i = tUserBookService.insertSelective(rUserBookTemp);
                    if (i == 0) {
                        result.put("save_success", 0); //插入失败
                    } else {
                        result.put("save_success", 1); //插入成功
                    }
                } else {
                    result.put("status", 0);
                }


            } catch (Exception e) {
                result.put("status", 0);
                e.printStackTrace();

            }

        } else {
            result.put("status", 0);
        }

        r.put("status", result);

        return r;
    }


    /**
     * scan/addShelfByM 手动录入
     * params:   isbn:
     *           addTime:
     *           bookName:
     *           author:
     *           publisher:
     *           pubTime:
     *           translator:
     *           price:
     *           briefInfo:
     *           number
     *
     * res: status:save_success 0 1
     **/

    @ResponseBody
    @GetMapping("scan/addShelfByM")
    public R addShelfByM(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/scan/addShelf");
        R r = new R();
        Map<String, Object> result = new HashMap<>();


        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        String isbn = params.get("isbn").toString();
        String taddTime = params.get("addTime").toString();
        String bookName = params.get("bookName").toString();
        String author = params.get("author").toString();
        String publisher = params.get("publisher").toString();
        String pubTime = params.get("pubTime").toString();
        String translator = params.get("translator").toString();
        String price = params.get("price").toString();
        String briefInfo = params.get("briefInfo").toString();
        Integer number = Integer.valueOf(params.get("number").toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date addTime = format.parse(taddTime);


        if (openId != null && isbn != null && bookName != null && addTime != null&& author != null&& publisher != null) {
            params.put("openId", openId);
            try {
                UserInf seMember = tMemberService.listPublic(params).get(0);
                if (seMember != null) {
                    BookInf bookInf = new BookInf();
                    bookInf.setBookName(bookName);
                    bookInf.setAuthor(author);
                    bookInf.setBriefIntro(briefInfo);
                    bookInf.setIsbn(isbn);
                    bookInf.setPrice(price);
                    bookInf.setPubTime(pubTime);
                    bookInf.setPublisher(publisher);
                    bookInf.setTranslator(translator);
                    rUserBook rUserBookTemp = new rUserBook();
                    rUserBookTemp.setAddTime(addTime);
                    rUserBookTemp.setIsbn(isbn);
                    rUserBookTemp.setOwnerId(seMember.getUserId());
                    int j = tBookService.insertSelective(bookInf);
                    int m=0;
                   for( m=0;m<number;m++){
                       int i = tUserBookService.insertSelective(rUserBookTemp);
                       if(i==1){
                           continue;
                       }else{
                           break;
                       }
                   }
                    if (m == number&&j==1) {
                        result.put("save_success", 1); //插入成功

                    } else {
                        result.put("save_success", 0); //插入失败
                    }
                } else {
                    result.put("status", 0);
                }


            } catch (Exception e) {
                result.put("status", 0);
                e.printStackTrace();

            }

        } else {
            result.put("status", 0);
        }

        r.put("status", result);

        return r;
    }



}
