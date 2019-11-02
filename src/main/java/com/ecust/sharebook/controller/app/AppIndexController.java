package com.ecust.sharebook.controller.app;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.ecust.sharebook.pojo.*;
import com.ecust.sharebook.service.*;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.JSONUtils;
import com.ecust.sharebook.utils.miniapp.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import com.ecust.sharebook.utils.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping("/small")
public class AppIndexController {

    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private WxMaService wxService;
    @Autowired
    private JwtUtil jwtUtil;



    /**
     * login
     **/
    @ResponseBody
    @RequestMapping("/login")
    public R login(HttpServletRequest req) throws Exception{
        System.out.println("/login");

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String code = req.getParameter("code");
        String mode = req.getParameter("mode");
        R r = new R();

        if (StringUtils.isBlank(code)) {
            System.out.println("code is empty");
            result.put("result", "-1");
            return r.put("data", result);
        }
        WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
        String openId = session.getOpenid();
        String sessionKey = session.getSessionKey();
        String unionId =session.getUnionid(); //null
        //jwt 加密 token
        UserInf jwtAccout = new UserInf();
        jwtAccout.setOpenId(openId);
        jwtAccout.setSessionKey(sessionKey);
        String token = jwtUtil.createTokenByWxAccount(jwtAccout);

        params.put("openId", openId);

        UserInf seMember = tMemberService.selectOne(params);

        if (seMember == null) {
            System.out.println("用户不存在");
            if(mode.equals("login")) {
                System.out.println("mode is login");
                //不存在就新建用户
                String userInfo = req.getParameter("userInfo");
                Map<String, Object> me = JSONUtils.jsonToMap(userInfo);
                seMember = new UserInf();
                if ("1".equals(me.get("gender").toString())) {
                    seMember.setGender(true);
                } else {
                    seMember.setGender(false);
                }
                seMember.setOpenId(openId);
                seMember.setUnionId(unionId);
                seMember.setSessionKey(sessionKey);
                seMember.setAvatarUrl(me.get("avatarUrl").toString());
                seMember.setCountry(me.get("country").toString());
                seMember.setProvince(me.get("province").toString());
                seMember.setCity(me.get("city").toString());
                seMember.setNickName(me.get("nickName").toString());
                seMember.setTel(null);
                int i = tMemberService.save(seMember);
                if(i==0) {
                    result.put("save_sucess", 0); //更新失败
                }else{
                    result.put("save_sucess", 1); //更新成功
                    r.put("userInfo",seMember);
                }
                result.put("token", token);
            }else{
                System.out.println("mode is tlogin");
            }

            result.put("result", 0);
        } else {
            System.out.println("用户存在");
            try {
                if(mode.equals("login")){
                    System.out.println("mode is login");
                }
                else{
                    params.put("sessionKey",sessionKey);
                    int i = tMemberService.updateSkeyByOpid(params);
                    if(i==0) {
                        result.put("save_sucess", 0); //更新失败
                    }else{
                        result.put("save_sucess", 1); //更新成功
                    }
                }
                result.put("token", token);
                result.put("result", 1);
            }catch (Exception e){
                e.printStackTrace();
                return R.error();
            }
        }
        r.put("data", result);
        return r;

    }


    public String info(String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        // 用户信息校验
        if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = this.wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }
}


