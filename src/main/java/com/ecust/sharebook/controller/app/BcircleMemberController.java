package com.ecust.sharebook.controller.app;


import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.circle.circleCatgBook;
import com.ecust.sharebook.pojo.util.circle.shelfByCatg;
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
import java.util.*;

@Controller
@RequestMapping("/small")
public class BcircleMemberController {

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

    /**
     * cfindshelf 书架列表
     *
     * @param "bookCircleId"
     * @return data --- "finallist "(1)
     *
     * status --- "is_exist" (1,0)
     **/
    @ResponseBody
    @GetMapping("/cfindshelf")

    public R cfindshelf(@RequestParam Map<String, Object> params) {
        System.out.println("/cfindshelf");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);

        //查询所有分类
        List<CategoryInf> cat_list = tCateService.list();

        try {
            List<Map<String, Object>> list = tBcircleMemberService.findShelf(param);

            List<circleCatgBook> finallist = new ArrayList<>();

            if (list.size() != 0 && list != null) {

                //分类--所有
                circleCatgBook circleCatgBookTemp0 = new circleCatgBook();
                List<shelfByCatg> shelfByCatglistTemp0 = new ArrayList<>();
                //书籍详情
                for (Map<String, Object> listTemp : list) {
                    shelfByCatg shelfByCatgTemp0New = new shelfByCatg();
                    BookInf bookInf = tBookService.selectByPrimaryKey(listTemp.get("isbn").toString());
                    System.out.println( bookInf.getBookName());
                    shelfByCatgTemp0New.setAll(bookInf.getIsbn(), bookInf.getBookName(), bookInf.getPicPath(),bookInf.getBriefIntro());
                    shelfByCatglistTemp0.add(shelfByCatgTemp0New);
                }
                circleCatgBookTemp0.setCatg_book_list(shelfByCatglistTemp0);

                circleCatgBookTemp0.setAll0(0, "所有",shelfByCatglistTemp0.size());
                finallist.add(circleCatgBookTemp0); //分类
                for (CategoryInf catgTemp : cat_list) {
                    circleCatgBook circleCatgBookTemp1 = new circleCatgBook();
                    circleCatgBookTemp1.setAll(catgTemp.getCatgId(), catgTemp.getCatgName());
                    finallist.add(circleCatgBookTemp1);
                }


                try {
                    List<circleCatgBook> mapList = tBcircleMemberService.findCatg(list);

                    for (circleCatgBook mapListTemp : mapList) {
                        for (circleCatgBook finallistTemp : finallist) {
                            if (mapListTemp.getCatgId() == finallistTemp.getCatgId()) {
                                finallistTemp.setCatg_book_list(mapListTemp.getCatg_book_list());
                                finallistTemp.setCount(mapListTemp.getCatg_book_list().size());
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                for (circleCatgBook finallistTemp : finallist) {
                    System.out.println(finallistTemp.getCount());
                }
                r.put("data", finallist); //isbn , total
                result.put("is_exist", 1);
            } else {
                System.out.println("无数据");
                result.put("is_exist", 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("无数据");
            result.put("is_exist", 0);
        }

        r.put("status", result);

        return r;
    }


    /**
     * findlist 书籍列表
     *
     * @param "bookCircleId，isbn"
     * @return data --- "map "(1)
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/findlist")
    public R findlist(@RequestParam Map<String, Object> params) {
        System.out.println("/findlist");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        String isbn = params.get("isbn").toString();
        param.put("bookCircleId", bookCircleId);
        param.put("isbn", isbn);

        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = tBcircleMemberService.findCatgShelf(param);
            r.put("data", list);
            result.put("is_exist", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0);
        }

        r.put("status", result);
        return r;
    }


    /**
     * findPostList 帖子列表
     *
     * @param "bookCircleId"
     * @return data --- "map "(1)
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/findPostList")
    public R findPostList(@RequestParam Map<String, Object> params) {
        System.out.println("/findPostList");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);


        List<PostInf> mlist = new ArrayList<>();

        try {
            mlist = tPostInfService.list(param);
            for (PostInf PostInftemp : mlist) {
                UserInf userInf = new UserInf();
                //userInf = tMemberService.selectByPrimaryKey(PostInftemp.getPublisherId());
                param.put("userId",PostInftemp.getPublisherId());
                userInf = tMemberService.listPublic(param).get(0);
                PostInftemp.setUserInf(userInf);

            }

            r.put("data", mlist);
            result.put("is_exist", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0);
        }

        r.put("status", result);
        return r;
    }


    /**
     * findPostDetail 帖子详情
     *
     * @param "postId"
     * @return commentList(1)
     *         postDetail(0)(1)
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/findPostDetail")
    public R findPostDetail(@RequestParam Map<String, Object> params) {
        System.out.println("/findPostDetail");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer postId = Integer.valueOf(params.get("postId").toString());
        param.put("postId", postId);


        PostInf plist = new PostInf();

        List<CommentInf> clist = new ArrayList<>();

        plist = tPostInfService.list(param).get(0);
        UserInf userInfTemp = new UserInf();
        userInfTemp = tMemberService.selectByPrimaryKey(plist.getPublisherId());
        plist.setUserInf(userInfTemp);
        r.put("postDetail", plist);

        try {
            clist = tCommentInfService.list(param);
            for (CommentInf commentInfTemp : clist) {
                UserInf userInf = new UserInf();
                userInf = tMemberService.selectByPrimaryKey(commentInfTemp.getUserId());
                commentInfTemp.setUserInf(userInf);
            }

            r.put("commentList", clist);
            result.put("is_exist", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0);
        }

        r.put("status", result);
        return r;
    }


    /**
     * reply 回复
     *
     * @param "postId", "userId" ," comeTime" ," content "
     * *@return commentList(1)
     *
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/reply")
    public R reply(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/reply");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String dateTime = params.get("comeTime").toString();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateTime);
        Integer postId = Integer.valueOf(params.get("postId").toString());
        Integer userId = Integer.valueOf(params.get("userId").toString());
        String content = params.get("content").toString();

        CommentInf commentInfTemps = new CommentInf();
        commentInfTemps.setUserId(userId);
        commentInfTemps.setComtTime(date);
        commentInfTemps.setContent(content);
        commentInfTemps.setPostId(postId);

       int i =  tCommentInfService.insert(commentInfTemps);
       if(i==1){
           result.put("is_exist", 1);
       }else{
           result.put("is_exist", 0);
       }

        List<CommentInf> clist = new ArrayList<>();
        try {
            clist = tCommentInfService.list(param);
            for (CommentInf commentInfTemp : clist) {
                UserInf userInf = new UserInf();
                userInf = tMemberService.selectByPrimaryKey(commentInfTemp.getUserId());
                commentInfTemp.setUserInf(userInf);
            }

            r.put("commentList", clist);

        } catch (Exception e) {
            e.printStackTrace();

        }

        r.put("status", result);
        return r;
    }


    /**
     * member 成员
     *
     * @param " bookCircleId "
     * @return commentList(1)
     *
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/member")
    public R member(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/member");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);

       // BookCircleInf bookCircleInf = tBookCircleService.selectByPrimaryKey(bookCircleId);
        //r.put("bookCircleInf",bookCircleInf);

        List<rBcircleMember> mlist = new ArrayList<>();

        try {
            mlist = tBcircleMemberService.list(param);
            for (rBcircleMember rBcircleMemberTemp : mlist) {
                UserInf userInf = new UserInf();
                param.clear();
                param.put("userId",rBcircleMemberTemp.getMemberId());
                userInf = tMemberService.listPublic(param).get(0);
                rBcircleMemberTemp.setUserInf(userInf);

            }

            r.put("data", mlist);
            result.put("is_exist", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0);
        }

        r.put("status", result);
        return r;

    }



    /**
     * addCircle 申请加圈
     *
     * @param "bookCircleId",access_token
     * @return
     *          status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/addCircle")
    public R addCircle(@RequestParam Map<String, Object> params) {
        System.out.println("/addCircle");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        param.put("openId", openId);
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
        return r;
    }



    /**
     * addFriend 申请加好友 (待完成)
     *
     * @param "",access_token
     * @return
     *          status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/addFriend")
    public R addFriend(@RequestParam Map<String, Object> params) {
        System.out.println("/addFriend");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        param.put("openId", openId);
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
        return r;
    }




}






