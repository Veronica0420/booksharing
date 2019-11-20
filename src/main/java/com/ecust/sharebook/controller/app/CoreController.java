package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.MessageInf;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rBookUserBorrow;
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
public class CoreController {

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
     * asecond/bdother/opt  申请借阅,取消申请,归还
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdother/opt"})
    public R opt(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdother/opt");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("save_success", -1);
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            //id值   apply :bookId ; other:  borrowId
            Integer id = Integer.valueOf(params.get("id").toString());
            String mode = params.get("mode").toString();
            String time = params.get("time").toString();
            Integer fid = Integer.valueOf(params.get("fid").toString());

            Map<String, Object> param = new HashMap<>();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(time);

            param.put("openId", openId);
            UserInf seMember = tMemberService.listPublic(param).get(0);
            param.clear();

            if (mode.equals("apply")) {

                //insert rBookUserBorrow
                rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                rBookUserBorrows.setBookId(id);
                rBookUserBorrows.setBorrowTime(date);
                rBookUserBorrows.setUserId(seMember.getUserId());
                //update rUserBook
                rUserBook rUserBooks = new rUserBook();
                rUserBooks.setBookId(id);
                rUserBooks.setBorrowState(1); //待处理

                //insert message
                MessageInf message = new MessageInf();
                message.setmType(0);//申请
                message.setSenderId(seMember.getUserId());  //我
                message.setReceiverId(fid);
                message.setDateTime(date);

                Boolean re = toptService.appplyBook(rBookUserBorrows, rUserBooks, message);
                if (re) {
                    result.put("save_success", 1); //更新成功
                } else {
                    result.put("save_success", 0); //更新失败
                }


            } else if (mode.equals("cancel")) { //取消申请

                //update rBookUserBorrow
                rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                rBookUserBorrows.setBorrowId(id);
                rBookUserBorrows.setUsrBorrowState(5);//取消申请


                Boolean re = toptService.cancelApply(rBookUserBorrows);
                if (re) {
                    result.put("save_success", 1); //更新成功
                } else {
                    result.put("save_success", 0); //更新失败
                }

            } else {
                //return

                //update rBookUserBorrow
                rBookUserBorrow rBookUserBorrows = new rBookUserBorrow();
                rBookUserBorrows.setBorrowId(id);
                rBookUserBorrows.setUsrBorrowState(3);//归还中

                //insert message
                MessageInf message = new MessageInf();
                message.setmType(1);//归还
                message.setSenderId(seMember.getUserId());  //我
                message.setReceiverId(fid);
                message.setDateTime(date);
                message.setmBorrowId(id);

                Boolean re = toptService.returnBook(rBookUserBorrows, message);
                if (re) {
                    result.put("save_success", 1); //更新成功
                } else {
                    result.put("save_success", 0); //更新失败
                }


            }


            r.put("data", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }


    /**
     * asecond/bdother/cancelApply  取消申请
     *
     * @params borrowId
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdother/cancelApply"})
    public R cancelApply(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdother/cancelApply");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();

            Integer borrowId = Integer.valueOf(params.get("borrowId").toString());

            boolean flag = toptService.cancelApply(borrowId);
            if (flag) {
                result.put("save_success", 1); //更新成功
            } else {
                result.put("save_success", 0); //更新失败
            }
            r.put("status", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }




    /**
     * asecond/bdother/apply  申请借阅
     *
     * @params bookId
     *         ownerId
     *         time
     *         userId
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdother/applyBook"})
    public R apply(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdother/applyBook");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();

            Integer bookId = Integer.valueOf(params.get("bookId").toString());

            Integer ownerId = Integer.valueOf(params.get("ownerId").toString());

            Integer userId = Integer.valueOf(params.get("userId").toString());

            String time = params.get("time").toString();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = format.parse(time);

           rBookUserBorrow rBookUserBorrowTemp = new rBookUserBorrow();
           rBookUserBorrowTemp.setBookId(bookId);
            rBookUserBorrowTemp.setUserId(userId);
            rBookUserBorrowTemp.setBorrowTime(date);
            rBookUserBorrowTemp.setUsrBorrowState(1);
            boolean flag= toptService.applyBook(rBookUserBorrowTemp,ownerId);
            if (flag) {
                result.put("save_success", 1); //更新成功
            } else {
                result.put("save_success", 0); //更新失败
            }
            r.put("status", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }





    /**
     * asecond/bdother/return  归还
     *
     * @params borrowId
     *         ownerId-RECEIVER
     *         userId-SENDER
     *         time：time
     **/

    @ResponseBody
    @GetMapping({"/asecond/bdother/return"})
    public R returnBook(@RequestParam Map<String, Object> params) {
        System.out.println("/asecond/bdother/return");
        R r = new R();
        try {
            Map<String, Object> result = new HashMap<>();

            Integer borrowId = Integer.valueOf(params.get("borrowId").toString());

            Integer ownerId = Integer.valueOf(params.get("ownerId").toString());

            Integer userId = Integer.valueOf(params.get("userId").toString());

            String time = params.get("time").toString();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = format.parse(time);

            MessageInf messageInf = new MessageInf();
            messageInf.setmBorrowId(borrowId);
            messageInf.setSenderId(userId);
            messageInf.setDateTime(date);
            messageInf.setReceiverId(ownerId);
            messageInf.setmType(1);
            boolean flag= toptService.returnBook(messageInf);
            if (flag) {
                result.put("save_success", 1); //更新成功
            } else {
                result.put("save_success", 0); //更新失败
            }
            r.put("status", result);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

}
