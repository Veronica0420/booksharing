package com.ecust.sharebook.utils.common;

import com.ecust.sharebook.pojo.*;

import java.util.*;

public class transferUtil {
    
    public static String  borrowState(Integer borrowState){
        String type = "";
        if (borrowState.equals(0)) {
            type = "无申请";
        } else if (borrowState.equals(1)) {
            type = "待处理";
        } else if (borrowState.equals(2)) {
            type = "已借出";
        }
        return type;
    }


    public static String  privacy(Integer privacy){
        String type = "";
        if (privacy.equals(0)) {
            type = "公开";
        } else if (privacy.equals(1)) {
            type = "私密";
        }
        return type;
    }

    public static String  time(Date date){
        String time = "";
        time = date.toString().split(" ")[0];
        return time;
    }


    public static String  usrBorrowState(Integer usrBorrowState){
        String type = "";
        if (usrBorrowState.equals(1)) {
            type = "审核中";
        } else if (usrBorrowState.equals(2)) {
            type = "借阅中";
        } else if (usrBorrowState.equals(3)) {
            type = "还书中";
        } else if (usrBorrowState.equals(4)) {
            type = "已还书";
        } else if (usrBorrowState.equals(5)) {
            type = "取消申请";
        } else if (usrBorrowState.equals(6)) {
            type = "申请不通过";
        } else if (usrBorrowState.equals(7)) {
            type = "归还失败";
        }
        return type;
    }


    public  static rUserBook setRUserBook(rUserBook temp){
        temp.setBorrowStateS(transferUtil.borrowState(temp.getBorrowState()));
        temp.setPrivacyS(transferUtil.privacy(temp.getPrivacy()));
        temp.setTime(transferUtil.time(temp.getAddTime()));

        return  temp;
    }


    public  static   rBookUserBorrow setBorrow(rBookUserBorrow temp){
        temp.setUsrBorrowStateS(transferUtil.usrBorrowState(temp.getUsrBorrowState()));

        temp.setBorrowTimeS(transferUtil.time(temp.getBorrowTime()));

        if(temp.getBorrowDateTime()!=null){
            temp.setBorrowDateTimeS(transferUtil.time(temp.getBorrowDateTime()));
        }
        if(temp.getReturnDateTime()!=null){
            temp.setReturnDateTimeS(transferUtil.time(temp.getReturnDateTime()));
        }

        return  temp;
    }

    public  static BookInf setBookInf(BookInf temp){
       if(temp.getBriefIntro()==null || temp.getBriefIntro().length()<=0){
           temp.setBriefIntro("暂无简介");
       }
        return  temp;
    }


    public static Map<String,List<FriendInf>>  friendlistType(List<FriendInf> lists,Integer Id){

        Map<String,List<FriendInf>> resultMap = new HashMap<>();

        for(FriendInf temp:lists){
            temp.setMid(Id);
            if(Id.equals(temp.getFid())){
                if(temp.getAliasf()!=null && temp.getAliasf().length()!=0 ){
                    temp.setName(temp.getAliasf());
                }else{
                    temp.setName(temp.getNickName());
                }
                temp.setId(temp.getUid());
            }else if(Id.equals(temp.getUid()) ){
                if(temp.getAliasu()!=null && temp.getAliasu().length()!=0 ){
                    temp.setName(temp.getAliasu());

                }else{
                    temp.setName(temp.getNickName());

                }
                temp.setId(temp.getFid());
            }
            if(resultMap.containsKey(PinyinUtil.getFirstLetter(temp.getName()))){
                resultMap.get(PinyinUtil.getFirstLetter(temp.getName())).add(temp);
            }else{
                //map中不存在，新建key，用来存放数据
                List<FriendInf> tmpList = new ArrayList<>();
                tmpList.add(temp);
                resultMap.put(PinyinUtil.getFirstLetter(temp.getName()), tmpList);
            }
        }



        return resultMap;
    }





}
