package com.ecust.sharebook.controller.app;


import com.ecust.sharebook.pojo.*;

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
import java.util.*;

@Controller
@RequestMapping("/small")
public class chatController {

    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TUserBookService tUserBookService;
    @Autowired
    private TBookService tBookService;
    @Autowired
    private TMessageService tMessageService;

    @Autowired
    private TBookUserBorrowService tBookUserBorrowService;
    @Autowired
    private optService toptService;


    /**
     * chat 查看申请
     * params: bookId
     * isbn
     * ownerId
     * res ：status:is_exist
     * data :messagelist
     * book:bookInf
     **/
    @ResponseBody
    @GetMapping({"/chat"})
    public R chat(@RequestParam Map<String, Object> params) {
        R r = new R();
        System.out.println("/chat");

        Integer ownerId = Integer.valueOf(params.get("ownerId").toString());
        Integer bookId = Integer.valueOf(params.get("bookId").toString());
        String isbn = params.get("isbn").toString();

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        Map<String, Object> userInfos = new HashMap<>();  //自己的信息
        Map<String, Object> fuserInfos = new HashMap<>();

        param.put("bookId", bookId);

        try {
            System.out.println(param);

            List<MessageInf> messageInflists = tMessageService.findCurrentBMessage(param);
            param.put("ownerfId", ownerId);
            BookInf bookInfs = tBookService.selectByPrimaryKey(isbn);

            if (messageInflists.size() != 0 && messageInflists != null) {
                for (MessageInf messageInf : messageInflists) {

                    param.clear();
                    param.put("userId", messageInf.getSenderId());
                    UserInf userInfs = tMemberService.listPublic(param).get(0);
                    messageInf.setAvatarUrl(userInfs.getAvatarUrl());
                    messageInf.setNickName(userInfs.getNickName());

                }
                status.put("is_exist", 1);

                r.put("data", messageInflists);
                r.put("book", bookInfs);
            } else {
                System.out.println("list-0");
                status.put("is_exist", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("chat--null");
            status.put("is_exist", 0);
        }


        r.put("status", status);
        return r;
    }

    /**
     * chat-apply 聊天界面 通过借阅
     * params:  messageId: ,
     * mType:  0 申请 1 借阅,
     * borrowRes 1 同意 2 拒绝
     * dateTime:
     * res ：status:is_exist
     * userBook
     * bookInf
     * userInfo{ nickName,avatarUrl }
     * borrow(other)
     **/
    @ResponseBody
    @GetMapping({"/chat/opt"})
    public R chatOpt(@RequestParam Map<String, Object> params) {
        R r = new R();
        System.out.println("/chat/opt");

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        try {

            Integer messageId = Integer.valueOf(params.get("messageId").toString());
            param.put("messageId",messageId);
           rBookUserBorrow rBookUserBorrowTemp= tBookUserBorrowService.selectByMessageId(param);
            Integer bookId =rBookUserBorrowTemp.getBookId();
            Integer mType = Integer.valueOf(params.get("mType").toString());
            Integer borrowRes =Integer.valueOf( params.get("borrowRes").toString());
            String dateTime = params.get("dateTime").toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(dateTime);



            if(mType==0){  //申请提醒
                if(borrowRes==1){//同意
                    Boolean flag=toptService.applyAgree(messageId,date,bookId);
                    if(flag){
                        status.put("is_exist", 1);
                    }else{
                        status.put("is_exist", 0);
                    }


                }else if(borrowRes==2) { //拒绝
                   Boolean flag = toptService.rejectApply(messageId);
                    if(flag){
                        status.put("is_exist", 1);
                    }else{
                        status.put("is_exist", 0);
                    }
                }

            }

            if(mType==1){  //归还提醒
                if(borrowRes==1){//同意
                    Boolean flag = toptService.returnAgree(messageId,date);
                    if(flag){
                        status.put("is_exist", 1);
                    }else{
                        status.put("is_exist", 0);
                    }

                }else if(borrowRes==2) { //拒绝
                    Boolean flag = toptService.rejectReturn(messageId,date);
                    if(flag){
                        status.put("is_exist", 1);
                    }else{
                        status.put("is_exist", 0);
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            status.put("is_exist", 0);
        }

        r.put("status", status);
        return r;
    }



    /**
     * chat/history 查看历史
     * params: bookId
     * isbn
     * ownerId
     * res ：status:is_exist
     * data :messagelist
     * book:bookInf
     **/
    @ResponseBody
    @GetMapping({"/chat/history"})
    public R chatHistory(@RequestParam Map<String, Object> params) {
        R r = new R();
        System.out.println("/chat/history");

        Integer ownerId = Integer.valueOf(params.get("ownerId").toString());
        Integer offset = Integer.valueOf(params.get("offset").toString());
        Integer limit = Integer.valueOf(params.get("limit").toString());
        Integer bookId = Integer.valueOf(params.get("bookId").toString());
        String isbn = params.get("isbn").toString();

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> status = new HashMap<>();

        param.put("bookId", bookId);
        param.put("limit",limit);
        param.put("offset",offset);

        try {
            System.out.println(param);

            List<MessageInf> messageInflists = tMessageService.findHistory(param);
            BookInf bookInfs = tBookService.selectByPrimaryKey(isbn);

            if (messageInflists.size() != 0 && messageInflists != null) {
                for (MessageInf messageInf : messageInflists) {
                    param.clear();
                    param.put("userId", messageInf.getSenderId());
                    UserInf userInfs = tMemberService.listPublic(param).get(0);
                    messageInf.setAvatarUrl(userInfs.getAvatarUrl());
                    messageInf.setNickName(userInfs.getNickName());

                }
                status.put("is_exist", 1);

                r.put("data", messageInflists);
                r.put("book", bookInfs);
            } else {
                System.out.println("list-0");
                status.put("is_exist", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("chat-history--null");
            status.put("is_exist", 0);
        }


        r.put("status", status);
        return r;
    }

    /**
     * contact  聊天界面 获取用户头像
     *
     * @param params userId  发送者Id
     *               touserId  接受者Id
     *               res ：status:is_exist(0,1)
     *               friendListType)(1)
     **/
    @ResponseBody
    @GetMapping({"/contact"})
    public R contact(@RequestParam Map<String, Object> params) {
        R r = new R();
        System.out.println("/contact");
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        Integer userId = Integer.valueOf(params.get("senderId").toString());
        Integer touserId = Integer.valueOf(params.get("receiverId").toString());
        Integer offset = Integer.valueOf(params.get("offset").toString());
        Integer limit = Integer.valueOf(params.get("receiverId").toString());
        param.put("userId", userId);
        UserInf userInfo = tMemberService.list(param).get(0);
        if (userInfo != null) {
            r.put("userInfo", userInfo);
        } else {
            result.put("is_exist", 0);
            r.put("status", result);
            return r;
        }
        param.clear();
        param.put("userId", touserId);
        UserInf fuserInfo = tMemberService.list(param).get(0);
        if (fuserInfo != null) {
            r.put("fuserInfo", fuserInfo);
        } else {
            result.put("is_exist", 0);
            r.put("status", result);
            return r;
        }
        param.clear();
        param.put("senderId", userId);
        param.put("receiverId", touserId);
        param.put("limit", limit);
        param.put("offset", offset);
        try {
            List<MessageInf> messageInfs = tMessageService.listAll(param);

            if (messageInfs.size() != 0 && messageInfs != null) {
for(MessageInf messageInfTemp:messageInfs){
    if(messageInfTemp.getmType()!=2){
        param.clear();
        param.put("messageId",messageInfTemp.getMessageId());
        messageInfTemp.setBookInf(tBookService.fingByMessageId(param));
    }
}

                r.put("list", messageInfs);
                r.put("count", messageInfs.size());
                result.put("is_exist", 1);
            } else {
                r.put("count", 0);
                result.put("is_exist", 0);
            }

        } catch (Exception e) {
            r.put("count", 0);
            e.printStackTrace();
            result.put("is_exist", 0);

        }


        r.put("status", result);
        return r;
    }



}
