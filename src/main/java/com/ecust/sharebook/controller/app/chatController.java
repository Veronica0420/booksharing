package com.ecust.sharebook.controller.app;


import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.pojo.util.chat.applyMessage;
import com.ecust.sharebook.pojo.util.chat.messageBookId;
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
     * chat 聊天界面
     * params:id
     * res ：status:is_exist
     *       userBook
     *       bookInf
     *       userInfo{ nickName,avatarUrl }
     *       borrow(other)
     *
     **/
    @ResponseBody
    @GetMapping({"/chat"})
    public R chat(@RequestParam Map<String, Object> params){
        R r=new R();
        System.out.println("/chat");
        try {
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
            //self --id ==borrowId   other --id == bookId
            Integer id = Integer.valueOf(params.get("id").toString());
            String mode = params.get("mode").toString();

            Map<String , Object> param = new HashMap<>();
            Map<String, Object> status = new HashMap<>();
            Map<String, Object> userInfos = new HashMap<>();  //自己的信息
            Map<String, Object> fuserInfos = new HashMap<>();

            if(mode.equals("oself")){

                try{
                    //根据bookid 查询 isbn
                    param.put("borrowId",id);
                    rBookUserBorrow rBookUserBorrowTemp =  tBookUserBorrowService.list(param).get(0);
                    if(rBookUserBorrowTemp!=null){
                        //根据bookid 查询 isbn
                        rUserBook rUserBookTemp =  tUserBookService.selectByPrimaryKey(rBookUserBorrowTemp.getBookId());
                        param.put("isbn",rUserBookTemp.getIsbn());
                        BookInf bookInfTemp = tBookService.list(param).get(0);
                        param.put("openId",openId);
                        UserInf userInf=tMemberService.list(param).get(0);//右
                        userInfos.put("nickName",userInf.getNickName());
                        userInfos.put("avatarUrl",userInf.getAvatarUrl());
                        param.clear();
                        param.put("userId",rUserBookTemp.getOwnerId()); //左
                        UserInf fuserInf=tMemberService.list(param).get(0);
                        fuserInfos.put("nickName",fuserInf.getNickName());
                        fuserInfos.put("avatarUrl",fuserInf.getAvatarUrl());
                        fuserInfos.put("userId",fuserInf.getUserId());

                        r.put("userBook",rBookUserBorrowTemp);
                        r.put("userInfo",userInfos);
                        r.put("fuserInfo",fuserInfos);
                        r.put("BookInf",bookInfTemp);

                        try{

                            BookInf bookInfTemp2= tBookService.findbyIsbn(param).get(0);
                            if(bookInfTemp2!=null){
                                status.put("is_exist",2); //成功查阅到书籍信息

                            }else status.put("is_exist",1); //查阅到书籍信息失败
                        } catch (Exception e){
                            System.out.println("bdother-self bookInfTemp==null");
                        }


                    }
                    else
                        status.put("is_exist",0); //bookid 不存在
                }catch (Exception e){
                    System.out.println("bbdother-self rBookUserBorrowTemp==null");
                }

                r.put("status",status);
            }else if(mode.equals("self")) {
                System.out.println("mode---self");
                param.put("bookId", id);
                rUserBook rUserBookTemp = tUserBookService.list(param).get(0);
                param.put("isbn", rUserBookTemp.getIsbn());
                BookInf bookInfTemp = tBookService.list(param).get(0); //r
                List<messageBookId> messageBookIdList = new ArrayList<>();

                if (rUserBookTemp.getBorrowState().equals(Integer.valueOf(1))) {
                    System.out.println("待处理1");
                    //待处理
                   List<MessageInf> messageInfList = tUserBookService.listByState1(param);

                   for(MessageInf me : messageInfList){
                       messageBookId messageBookIdTemp = new messageBookId();
                       param.clear();
                       param.put("userId",me.getSenderId());
                       UserInf frUserInf =tMemberService.list(param).get(0);
                       messageBookIdTemp.setOther(frUserInf.getUserId(),
                               frUserInf.getNickName(),frUserInf.getAvatarUrl(),me);
                       messageBookIdList.add(messageBookIdTemp);
                   }
                    applyMessage  applyMessageTemp = new applyMessage(
                            bookInfTemp.getBookName(),
                            bookInfTemp.getAuthor(),
                            bookInfTemp.getPicPath(),
                            messageBookIdList,
                            0,messageBookIdList.size());
                    status.put("is_exist",2);
                    r.put("status",status);
                    r.put("data",applyMessageTemp);

                } else if (rUserBookTemp.getBorrowState().equals(Integer.valueOf(2))) {
                    System.out.println("//已借出3");
                    //已借出
                    List<MessageInf> messageInfList =tUserBookService.listByState2(param);//借阅中，归还中，23  唯一；


                    for(MessageInf me : messageInfList){
                        messageBookId messageBookIdTemp = new messageBookId();
                        param.clear();
                        param.put("userId",me.getSenderId());
                        UserInf frUserInf =tMemberService.list(param).get(0);
                        messageBookIdTemp.setOther(frUserInf.getUserId(),
                                frUserInf.getNickName(),frUserInf.getAvatarUrl(),me);
                        messageBookIdList.add(messageBookIdTemp);
                    }
                    applyMessage  applyMessageTemp = new applyMessage(
                            bookInfTemp.getBookName(),
                            bookInfTemp.getAuthor(),
                            bookInfTemp.getPicPath(),
                            messageBookIdList,
                            1,messageBookIdList.size());

                    status.put("is_exist",2);
                    r.put("status",status);
                    r.put("data",applyMessageTemp);

                } else if (rUserBookTemp.getBorrowState().equals(Integer.valueOf(0))) {
                    //无申请
                    status.put("is_exist",3);
                    r.put("status",status);

                }


            }


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

    /**
     * chat-apply 聊天界面 通过借阅
     * params:messageId,mode
     * res ：status:is_exist
     *       userBook
     *       bookInf
     *       userInfo{ nickName,avatarUrl }
     *       borrow(other)
     *
     **/
    @ResponseBody
    @GetMapping({"/chat/opt"})
    public R chatOpt(@RequestParam Map<String, Object> params){
        R r=new R();
        System.out.println("/chat/opt");
        try {

            Integer messageId = Integer.valueOf(params.get("messageId").toString());
            String mode = params.get("mode").toString();
            String type = params.get("type").toString();
            String dateTime = params.get("dateTime").toString();
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(dateTime);


            Map<String , Object> param = new HashMap<>();
            Map<String, Object> status = new HashMap<>();

            Boolean i =false;

            param.put("messageId",messageId);
            if(mode.equals("apply")){
                if(type.equals("pass"))
                     i = toptService.updateByOpt(messageId,true,true,date);
                else //reject
                    i = toptService.updateByOpt(messageId,true,false,date);

            }else{
                //return
                if(type.equals("pass"))
                    i = toptService.updateByOpt(messageId,false,true,date);
                else
                    i = toptService.updateByOpt(messageId,false,false,date);

            }

            if(i){
                status.put("success",1);
            }else{
                status.put("success",0);
            }
            r.put("status",status);


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



}
