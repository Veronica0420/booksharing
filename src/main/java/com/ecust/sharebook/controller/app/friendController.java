package com.ecust.sharebook.controller.app;

import com.ecust.sharebook.pojo.FriendInf;
import com.ecust.sharebook.pojo.UserInf;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     *
     * @params openId--> userId
     * res ：status:is_exist(0,1)
     * friendListType)(1)
     **/
    @ResponseBody
    @GetMapping({"/friendlist"})
    public R findList(@RequestParam Map<String, Object> params) {
        R r = new R();
        System.out.println("/friendlist");
        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        param.put("openId", openId);
        UserInf userInf = tMemberService.list(param).get(0);
        param.put("userId", userInf.getUserId());
        try {
            List<FriendInf> friendLists = tFriendService.getList(param);
            if (friendLists != null && friendLists.size() != 0) {
                result.put("is_exist", 1);
                //以首字母分组返回
                Map<String, List<FriendInf>> resultMap = transferUtil.friendlistType(friendLists, userInf.getUserId());

                List<friendListType> friendListTypes = new ArrayList<>();

                for (Map.Entry<String, List<FriendInf>> entry : resultMap.entrySet()) {
                    String mapKey = entry.getKey();
                    List<FriendInf> mapValue = entry.getValue();
                    friendListType friendListTypeTemp = new friendListType(mapKey, mapValue);
                    friendListTypes.add(friendListTypeTemp);
                }
                Collections.sort(friendListTypes);
                r.put("data", friendListTypes);
            } else {
                result.put("is_exist", 0); //无好友
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("is_exist", 0); //无好友
        }

        r.put("status", result);


        return r;
    }


    /**
     * friendInfo  好友个人信息界面
     *
     * @params fid
     * @params mid
     * <p>
     * res ：status:is_exist(0,1)
     * userInf)(1)
     **/
    @ResponseBody
    @GetMapping({"/friendInfo"})
    public R userInfo(@RequestParam Map<String, Object> params) {
        R r = new R();
        System.out.println("/friendInfo");
        Integer fid = Integer.valueOf(params.get("fid").toString());

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        param.put("openId", openId);
        UserInf userInf = tMemberService.list(param).get(0);
        param.clear();
        param.put("mid", userInf.getUserId());
        param.put("fid", fid);
        int flag = -1;
        if (fid == userInf.getUserId()) {
            flag=0;
            r.put("flag", 0); //self
        } else {
            try {

                FriendInf friendInf = tFriendService.list2(param).get(0);
                if (friendInf != null) {
                    if (friendInf.getOptType() == 1) {
                        flag = 2;
                        r.put("flag", 2); //agree
                    } else {
                        flag =1;
                        r.put("flag", 1); // apply
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("陌生");
                flag = 3;
                r.put("flag", 3);
            }
        }

        if(flag==2){
            try {
                Map<String, Object> friendInfo = tFriendService.friendInfo(param);
                if (friendInfo != null) {
                    String name = "";
                    if (Integer.valueOf(friendInfo.get("fid").toString()).equals(fid)) {
                        if (friendInfo.get("aliasu") != null&&friendInfo.get("aliasu").toString().length()!=0) {
                            name = friendInfo.get("aliasu").toString();
                        } else {
                            name = "暂无";
                        }
                        friendInfo.put("id", friendInfo.get("fid"));
                        r.put("mid", friendInfo.get("uid"));

                    } else {
                        friendInfo.put("id", friendInfo.get("uid"));
                        r.put("mid", friendInfo.get("fid"));
                        if (friendInfo.get("aliasf") != null&&friendInfo.get("aliasf").toString().length()!=0) {
                            name = friendInfo.get("aliasf").toString();
                        } else {
                            name = "暂无";
                        }

                    }
                    friendInfo.put("name", name);
                    result.put("is_exist", 1);
                    r.put("data", friendInfo);

                } else {
                    result.put("is_exist", 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.put("is_exist", 0);
            }
        }
        else{
            param.put("userId", fid);
            try {
                List<UserInf> userInfs = tMemberService.listPublic(param);
                if (userInfs != null && userInfs.size() != 0) {
                    r.put("mid",userInf.getUserId());
                    r.put("data", userInfs.get(0));
                    result.put("is_exist", 1);
                } else {
                    result.put("is_exist", 0);
                }
            } catch (Exception e) {
                System.out.println("unfriendInfo--null");
                result.put("is_exist", 0);
            }

        }

        r.put("status", result);
        return r;
    }


    /**
     * friendOpt  好友个人信息界面-删除/添加/同意/编辑昵称
     *
     * @params fid
     * @params mid
     * @params mode "add" "delete"
     * @params addtime ---"add"
     * res ：status:is_exist(0,1)
     **/
    @ResponseBody
    @GetMapping({"/friendOpt"})
    public R friendOpt(@RequestParam Map<String, Object> params) throws ParseException {
        R r = new R();
        System.out.println("/friendInfo");
        Integer fid = Integer.valueOf(params.get("fid").toString());
        Integer mid = Integer.valueOf(params.get("mid").toString());
        String mode = params.get("mode").toString();

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();


        int type = -1;
        if (mode.equals("delete") || mode.equals("agree") || mode.equals("edit")) {
            try {
                param.put("fid", fid);
                param.put("mid", mid);


                List<FriendInf> friendInfs = tFriendService.list2(param);
                if (friendInfs != null && friendInfs.size() != 0) {
                    //update optTYpe
                    FriendInf friendInf = new FriendInf();
                    System.out.println(friendInfs.get(0).getFriendId());
                    friendInf.setFriendId(friendInfs.get(0).getFriendId());

                    if (mode.equals("delete")) {
                        friendInf.setOptType(2);
                    } else if (mode.equals("agree")) {

                        friendInf.setOptType(1);
                    } else if (mode.equals("edit")) {


                        String name = params.get("name").toString();
                        if (mid == friendInfs.get(0).getUid()) {
                            friendInf.setAliasu(name);
                        } else {
                            friendInf.setAliasf(name);
                        }
                    }

                    type = tFriendService.updateByPrimaryKeySelective(friendInf);

                    if (type == 1) {
                        result.put("is_exist", 1);

                    } else {
                        result.put("is_exist", 0);

                    }

                } else {
                    result.put("is_exist", 0);

                }
            } catch (Exception e) {
                System.out.println("null");
                e.printStackTrace();
                result.put("is_exist", 0);

            }
        } else if (mode.equals("add")) {
            String time = params.get("addTime").toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date addTime = format.parse(time);

            FriendInf friendInf = new FriendInf();
            friendInf.setUid(mid);
            friendInf.setFid(fid);
            friendInf.setAddTime(addTime);
            System.out.println("mid" + mid + "fid" + fid + "date" + addTime);
            try {
                type = tFriendService.insertSelective(friendInf);

                if (type == 1) {
                    result.put("is_exist", 1);

                } else {
                    result.put("is_exist", 0);

                }
            } catch (Exception e) {
                result.put("is_exist", 0);

            }

        }

        r.put("status", result);


        return r;
    }


    /**
     * fMessageList  好友个人信息界面-新消息
     *
     * @params
     * @params mode "little" "all"
     * res ：status:is_exist(0,1)
     **/
    @ResponseBody
    @GetMapping({"/fMessageList"})
    public R friendMessageList(@RequestParam Map<String, Object> params) throws ParseException {
        R r = new R();
        System.out.println("/fMessageList");

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        String mode = params.get("mode").toString();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0);
        param.put("userId", me.getUserId());


        if (mode.equals("little")) {
            try {

                List<FriendInf> friendInfs = tFriendService.messageList(param);
                if (friendInfs != null && friendInfs.size() != 0) {

                    for (FriendInf temp : friendInfs) {
                        if (temp.getUid() == me.getUserId()) {
                            temp.setUtf(true);
                            if (temp.getAliasu() != null && temp.getAliasu().length() != 0) {
                                temp.setName(temp.getAliasu());
                            } else {
                                temp.setName(temp.getNickName());
                            }

                            temp.setId(temp.getFid());
                            temp.setMid(temp.getUid());
                        } else {
                            if (temp.getAliasf() != null && temp.getAliasf().length() != 0) {
                                temp.setName(temp.getAliasf());
                            } else {
                                temp.setName(temp.getNickName());
                            }
                            temp.setId(temp.getUid());
                            temp.setMid(temp.getFid());
                            temp.setUtf(false);
                        }
                    }

                    r.put("data", friendInfs);

                    result.put("is_exist", 1);
                } else {
                    result.put("is_exist", 0);

                }
            } catch (Exception e) {
                result.put("is_exist", 0);

            }
        } else if (mode.equals("all")) {
            try {

                List<FriendInf> friendInfs = tFriendService.messageAllList(param);
                if (friendInfs != null && friendInfs.size() != 0) {

                    for (FriendInf temp : friendInfs) {
                        if (temp.getUid() == me.getUserId()) {
                            temp.setUtf(true);
                            if (temp.getAliasu() != null && temp.getAliasu().length() != 0) {
                                temp.setName(temp.getAliasu());
                            } else {
                                temp.setName(temp.getNickName());
                            }
                            temp.setMid(temp.getUid());
                            temp.setId(temp.getFid());
                        } else {
                            if (temp.getAliasf() != null && temp.getAliasf().length() != 0) {
                                temp.setName(temp.getAliasf());
                            } else {
                                temp.setName(temp.getNickName());
                            }
                            temp.setMid(temp.getFid());
                            temp.setId(temp.getUid());
                            temp.setUtf(false);
                        }

                    }
                    r.put("data", friendInfs);
                    result.put("is_exist", 1);

                } else {
                    result.put("is_exist", 0);

                }
            } catch (Exception e) {
                result.put("is_exist", 0);

            }

        }
        r.put("status", result);


        return r;
    }


    @ResponseBody
    @GetMapping("/unfriendInfo")
    public R unfriendInfo(@RequestParam Map<String, Object> params) {
        System.out.println("/unfriendInfo");

        R r = new R();
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

        param.put("openId", openId);
        UserInf me = tMemberService.list(param).get(0);
        param.clear();

        Integer userId = Integer.valueOf(params.get("userId").toString());
        param.put("userId", userId);
        try {
            List<UserInf> userInfs = tMemberService.listPublic(param);
            if (userInfs != null && userInfs.size() != 0) {
                r.put("mid", me.getUserId());
                r.put("data", userInfs.get(0));
                result.put("is_exist", 1);
            } else {
                result.put("is_exist", 0);
            }
        } catch (Exception e) {
            System.out.println("unfriendInfo--null");
            result.put("is_exist", 0);
        }

        r.put("status", result);


        return r;

    }

}
