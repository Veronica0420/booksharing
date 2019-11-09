package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.BookInf;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.pojo.rUserBook;
import com.ecust.sharebook.pojo.util.friend.friendList;
import com.ecust.sharebook.pojo.util.friend.friendListType;
import com.ecust.sharebook.service.TFriendService;
import com.ecust.sharebook.service.TMemberService;
import com.ecust.sharebook.utils.common.R;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/small")
public class friendController {

    @Autowired
    private TFriendService tFriendService;

    @Autowired
    private TMemberService tMemberService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * friendlist  好友列表
     * params:userId
     * res ：status:is_exist
     *       friendlist
     *
     **/
    @ResponseBody
    @GetMapping({"/friendlist"})
    public R findList(@RequestParam Map<String, Object> params){
        R r=new R();
        System.out.println("/friendlist");
        try {
            String openId = jwtUtil.getWxOpenIdByToken( params.get("access_token").toString());
            Map<String , Object> param = new HashMap<>();
            Map<String, Object> result = new HashMap<>();
            param.put("openId",openId);
            UserInf userInf=tMemberService.list(param).get(0);
            param.put("userId",userInf.getUserId());


               List<friendList> friendLists = tFriendService.getList(param);

               if(friendLists!=null && friendLists.size()!=0){
                   System.out.println(friendLists.get(0));
                   result.put("is_exist",1);
                   //以首字母分组返回
                   Map<String,List<friendList>> resultMap = new HashMap<>();
                   for(friendList temp : friendLists){
                       //map中已存在
                       if(resultMap.containsKey(temp.getFirstChar())){
                           resultMap.get(temp.getFirstChar()).add(temp);
                       }else{
                           //map中不存在，新建key，用来存放数据
                           List<friendList> tmpList = new ArrayList<>();
                           tmpList.add(temp);
                           resultMap.put(temp.getFirstChar(), tmpList);
                       }
                   }
                   List<friendListType> friendListTypes = new ArrayList<>();

                   for(Map.Entry<String, List<friendList>> entry : resultMap.entrySet()){
                       String mapKey = entry.getKey();
                       List<friendList> mapValue = entry.getValue();
                       friendListType friendListTypeTemp = new friendListType(mapKey,mapValue);
                       friendListTypes.add(friendListTypeTemp);
                   }
                   Collections.sort(friendListTypes);
                   r.put("data",friendListTypes);


                   }
               else{
                   result.put("is_exist",0); //无好友
                }


            r.put("status",result);


        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
        return r;
    }

}
