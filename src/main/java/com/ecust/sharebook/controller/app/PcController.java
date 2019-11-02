package com.ecust.sharebook.controller.app;


import cn.binarywang.wx.miniapp.api.WxMaService;
import com.ecust.sharebook.pojo.UserInf;
import com.ecust.sharebook.service.TMemberService;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import com.ecust.sharebook.utils.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/small")
public class PcController {

    @Autowired
    private TMemberService tMemberService;
    @Autowired
    private WxMaService wxService;
    @Autowired
    private JwtUtil jwtUtil;


    @ResponseBody
    @GetMapping("/PC")
    public R PCshow(@RequestParam Map<String, Object> params) {
        System.out.println("/PC");
        R r = new R();
        try {
            String openId = jwtUtil.getWxOpenIdByToken(params.get("access_token").toString());

            Map<String, Object> param = new HashMap<>();
            Map<String, Object> result = new HashMap<>();
            param.put("openId", openId);

            if (openId != null) {
                try {
                    List<UserInf> userInfs = tMemberService.list(param); //查询用户信息（根据openId 查询 userId）

                    if (userInfs != null && userInfs.size() != 0) {
                        UserInf seMember = userInfs.get(0);
                        result.put("isSuccess", 2);
                        r.put("userInfo", seMember);
                    }
                } catch (Exception e) {
                    result.put("isSuccess", 1);
                }

                r.put("status", result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(("无数据"));
            return R.error();
        }
        return r;
    }


}
