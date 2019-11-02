package com.ecust.sharebook.controller.app;


import com.ecust.sharebook.pojo.*;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            }else if(mode.equals("self")){
                param.put("openId",openId);
                UserInf userInf=tMemberService.list(param).get(0);//右

                param.put("bookId",id);
                param.put("limit",Integer.valueOf(params.get("limited").toString()));
                param.put("offset",Integer.valueOf(params.get("offset").toString()));

                //查看借阅中的书籍
                param.put("usrBorrowState",Integer.valueOf(1)) ;//1,2,3
                param.put("ownerId",userInf.getUserId());

                System.out.println(param);
                try{
                    List<Integer> borrowIdlists = tUserBookService.listByState(param);
                    List <messageBookId> messageBookIdLists = new ArrayList<>();
                    //一个borrowid 对应两个 messageId (申请/归还)
                    if(borrowIdlists.size()!=0 && borrowIdlists!=null){

                        for(Integer is : borrowIdlists){
                            System.out.println("borrowIdlists--!null"+is);
                            param.clear();
                            param.put("mBorrowId",is);
                            List<MessageInf> messageInfs = tMessageService.list(param);
                            MessageInf current = new MessageInf();
                            if(messageInfs!=null&& messageInfs.size()!=0){
                                if(messageInfs.size()==2){
                                    for(MessageInf mis :messageInfs ){
                                        if(mis.getmType().equals(Integer.valueOf(1))){
                                            current = mis;
                                        }
                                    }

                                }else
                                    current = messageInfs.get(0);

                                messageBookId messageBookIdTemp = new messageBookId(
                                        Integer.valueOf(tMemberService.get(current.getReceiverId()).getUserId().toString()),
                                        tMemberService.get(current.getReceiverId()).getNickName(),
                                        tMemberService.get(current.getReceiverId()).getAvatarUrl(),
                                        current
                                );
                                messageBookIdLists.add(messageBookIdTemp);
                            }else{
                                System.out.println("null");
                            }

                        }
                    }else{
                        System.out.println("borrowIdlists--null");
                    }


                    r.put("data",messageBookIdLists);
                    status.put("is_exist",2);
                }catch (Exception e){
                    e.printStackTrace();
                    status.put("is_exist",2);
                    System.out.println("无正在借阅书籍");
                }
                r.put("status",status);



            }else{

            }


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }



}
