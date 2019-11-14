package com.ecust.sharebook.controller.app;


import com.alibaba.fastjson.JSONArray;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.shelf.book;
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
    private TFriendService tFriendService;


    /**
     * cfindshelf 书架列表
     *
     * @param "bookCircleId"
     * @return data --- "finallist "(1)
     * <p>
     * status --- "is_exist" (1,0)
     **/
    @ResponseBody
    @GetMapping("/cfindshelf")

    public R cfindshelf(@RequestParam Map<String, Object> params) {
        System.out.println("/cfindshelf");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);

        try {
            List<book> bookList = tBcircleMemberService.findShelfCircle(param);

            param.clear();
            param.put("openId", openId);
            UserInf me = tMemberService.listPublic(param).get(0);

            if (bookList.size() != 0 && bookList != null) {
                for (book bookMtemp : bookList) {
                    if (bookMtemp.getOwnerId() == me.getUserId()) {
                        bookMtemp.setMe(true);
                    } else {
                        bookMtemp.setMe(false);
                    }
                    BookInf bookInf = tBookService.selectByPrimaryKey(bookMtemp.getIsbn());
                    bookMtemp.setAuthor(bookInf.getAuthor());
                    bookMtemp.setBookName(bookInf.getBookName());
                    bookMtemp.setPicPath(bookInf.getPicPath());
                }

                r.put("data", bookList); //isbn , total
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

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());


        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        String isbn = params.get("isbn").toString();
        param.put("bookCircleId", bookCircleId);
        param.put("isbn", isbn);

        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = tBcircleMemberService.findCircleBookList(param);
            param.clear();
            param.put("openId", openId);
            UserInf me = tMemberService.listPublic(param).get(0);
            for (Map<String, Object> temp : list) {
                Integer ownerId = Integer.valueOf(temp.get("ownerId").toString());

                temp.put("sortTime", temp.get("sortTime").toString().split(" ")[0]);

                if (ownerId == me.getUserId()) {
                    temp.put("type", 1);
                } else {
                    param.clear();
                    param.put("userId", ownerId);
                    UserInf owner = tMemberService.listPublic(param).get(0);
                    temp.put("type", 0);
                    temp.put("name", owner.getNickName());
                    temp.put("pic", owner.getAvatarUrl());
                }
            }

            r.put("data", list);
            result.put("isSuccess", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("isSuccess", 0);
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
        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0);

        List<PostInf> mlist = new ArrayList<>();

        try {
            mlist = tPostInfService.Plist(param);
            for (PostInf PostInftemp : mlist) {
                UserInf userInf = new UserInf();
                param.clear();
                //userInf = tMemberService.selectByPrimaryKey(PostInftemp.getPublisherId());
                param.put("userId", PostInftemp.getPublisherId());
                userInf = tMemberService.listPublic(param).get(0);
                PostInftemp.setUserInf(userInf);

                if (PostInftemp.getPublisherId().equals(me.getUserId())) {
                    PostInftemp.setFlag(0);
                } else {
                    PostInftemp.setFlag(2);
                }

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
     * findPost 帖子详情
     *
     * @param "postId"
     * @return commentList(1)
     * postDetail(0)(1)
     * isFriend{0,1,2} (1)(0)
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/findPost")
    public R findPostDetail(@RequestParam Map<String, Object> params) {
        System.out.println("/findPostDetail");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer postId = Integer.valueOf(params.get("postId").toString());
        param.put("postId", postId);
        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0);


        try {
            PostInf plist = tPostInfService.post(param);
            r.put("data", plist);

            param.clear();
            param.put("userId", plist.getPublisherId());
            UserInf userInf = tMemberService.listPublic(param).get(0);
            plist.setUserInf(userInf);

            if (plist.getPublisherId().equals(me.getUserId())) {
                plist.setFlag(0); //本人
            } else {
                try {
                    param.clear();
                    param.put("fid", plist.getPublisherId());
                    param.put("uid", me.getUserId());
                    List<FriendInf> friends = tFriendService.list3(param);
                    if (friends != null && friends.size() != 0) {
                        plist.setFlag(1); //朋友
                    } else {
                        plist.setFlag(2);//陌生人
                    }

                } catch (Exception e) {
                    plist.setFlag(2);//陌生人
                }
            }

            result.put("is_exist", 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0);
        }

        r.put("status", result);
        return r;
    }


    /**
     * findCommentList 评论列表
     *
     * @param "postId"
     * @return commentList(1)
     * postDetail(0)(1)
     * isFriend{0,1,2} (1)(0)
     * status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/findCommentList")
    public R findCommentList(@RequestParam Map<String, Object> params) {
        System.out.println("/findCommentList");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer postId = Integer.valueOf(params.get("postId").toString());


        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0);
        param.clear();


        List<CommentInf> clist = new ArrayList<>();
        try {
            param.put("postId", postId);
            clist = tCommentInfService.list(param);
            for (CommentInf commentInfTemp : clist) {
                UserInf userInf = new UserInf();
                param.put("userId", commentInfTemp.getUserId());
                userInf = tMemberService.listPublic(param).get(0);
                commentInfTemp.setUserInf(userInf);
                commentInfTemp.setComtTimeS(transferUtil.time(commentInfTemp.getComtTime()));
                if (me.getUserId().equals(commentInfTemp.getUserId())) {
                    commentInfTemp.setFlag(0); //自己
                } else {
                    commentInfTemp.setFlag(1);
                }
            }
            r.put("data", clist);
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
     * @param "postId", " comeTime" ," content "
     *                  *@return commentList(1)
     *                  <p>
     *                  status --- "is_exist" (1,0)
     **/

    @ResponseBody
    @GetMapping("/reply")
    public R reply(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/reply");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0); //查询用户信息（根据openId 查询 userId）
        Integer userId = me.getUserId();

        String dateTime = params.get("comeTime").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateTime);
        Integer postId = Integer.valueOf(params.get("postId").toString());

        String content = params.get("content").toString();

        CommentInf commentInfTemps = new CommentInf();
        commentInfTemps.setUserId(userId);
        commentInfTemps.setComtTime(date);
        commentInfTemps.setContent(content);
        commentInfTemps.setPostId(postId);

        int i = tCommentInfService.insert(commentInfTemps);
        if (i == 1) {
            result.put("is_exist", 1);
        } else {
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
     * <p>
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

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0); //查询用户信息（根据openId 查询 userId）


        List<rBcircleMember> mlist = new ArrayList<>();

        try {
            mlist = tBcircleMemberService.list(param);
            for (rBcircleMember rBcircleMemberTemp : mlist) {
                UserInf userInf = new UserInf();
                param.clear();
                param.put("userId", rBcircleMemberTemp.getMemberId());
                userInf = tMemberService.listPublic(param).get(0);
                rBcircleMemberTemp.setUserInf(userInf);

                param.clear();

                param.put("fid", rBcircleMemberTemp.getMemberId());
                param.put("mid", me.getUserId());
                if (rBcircleMemberTemp.getMemberId().equals(me.getUserId())) {
                    rBcircleMemberTemp.setFriend(0);
                } else {
                    try {
                        List<FriendInf> lists = tFriendService.list2(param);
                        if (lists != null && lists.size() != 0) {
                            if (lists.get(0).getOptType().equals(0)) {
                                rBcircleMemberTemp.setFriend(1);
                            } else {
                                rBcircleMemberTemp.setFriend(2);
                            }

                        } else {
                            rBcircleMemberTemp.setFriend(3);
                        }

                    } catch (Exception e) {
                        rBcircleMemberTemp.setFriend(3);
                    }
                }

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
     * @return status --- "is_exist" (1,0)
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

        int i = tBcircleMemberService.insert(rBcircleMemberTemp);
        if (i == 1) {
            result.put("is_exist", 1);
        } else
            result.put("is_exist", 0);


        r.put("status", result);
        return r;
    }


    /**
     * addFriend 申请加好友 (待完成)
     *
     * @param "",access_token
     * @return status --- "is_exist" (1,0)
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

        int i = tBcircleMemberService.insert(rBcircleMemberTemp);
        if (i == 1) {
            result.put("is_exist", 1);
        } else
            result.put("is_exist", 0);


        r.put("status", result);
        return r;
    }


    @ResponseBody
    @GetMapping("/addPost")
    public R addPost(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/addPost");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();


        String dateTime = params.get("time").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateTime);

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        String content = params.get("content").toString();
        String title = params.get("title").toString();

        PostInf postInf = new PostInf();
        postInf.setTitle(title);
        postInf.setContent(content);
        postInf.setPubTime(date);
        postInf.setBookCircleId(bookCircleId);

        try {
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            param.put("openId", openId);
            UserInf me = tMemberService.list(param).get(0); //查询用户信息（根据openId 查询 userId）
            Integer userId = me.getUserId();
            postInf.setPublisherId(userId);

            int i = tPostInfService.insertSelective(postInf);
            if (i == 1) {
                result.put("is_exist", 1);
            } else {
                result.put("is_exist", 0);
            }

        } catch (Exception e) {
            result.put("is_exist", 0);
            System.out.println("addPost---null");
            e.printStackTrace();
        }

        r.put("status", result);
        return r;
    }


    @ResponseBody
    @GetMapping("/deleteCMember")
    public R deleteCMember(@RequestParam Map<String, Object> params) throws ParseException {
        System.out.println("/deleteCMember");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String list = params.get("list").toString();

        List<Integer> lists= JSONArray.parseArray(list,Integer.class);
      //  System.out.println(lists);
        try{
            int i = tBcircleMemberService.deletelist(lists);
            if(i==1){
                result.put("is_exist", 1);
            }else {
                result.put("is_exist", 0);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("deleteCMember---null");
        }

        r.put("status", result);
        return r;

    }



    @ResponseBody
    @GetMapping("/applyCircle")
    public R applyCircle(@RequestParam Map<String, Object> params) {
        System.out.println("/applyCircle");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Integer bookCircleId = Integer.valueOf(params.get("bookCircleId").toString());
        param.put("bookCircleId", bookCircleId);
        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        param.put("openId", openId);
        try {
            UserInf me = tMemberService.list(param).get(0);

            rBcircleMember rBcircleMemberTemp = new rBcircleMember();
            rBcircleMemberTemp.setBookCircleId(bookCircleId);
            rBcircleMemberTemp.setMemberId(me.getUserId());
            rBcircleMemberTemp.setIfCreater(0);

            int i = tBcircleMemberService.insertSelective(rBcircleMemberTemp);

            if (i == 1) {
                result.put("is_exist", 1);
            } else {
                result.put("is_exist", 0);
            }


        } catch (Exception e) {
            result.put("is_exist", 0);
        }
        r.put("status", result);
        return r;
    }


}






