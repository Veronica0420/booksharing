package com.ecust.sharebook.controller;

import com.ecust.sharebook.utils.MD5Utils;
import com.ecust.sharebook.utils.R;
import com.ecust.sharebook.utils.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class LoginController  {
    //定义logger，打印日志用的
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping({"/login","/"}) //登陆界面
    String login(){
        return  "login" ;
    }

    @GetMapping({ "/index" })
    String index() {
        return "index_v1";
    }

    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username,String password ){
        R.ok("controll");
       // password = MD5Utils.encrypt(username,password); //加密
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            R.ok("token");
            subject.login(token);
            return  R.ok();
        }catch (AuthenticationException e){
            return R.error("用户密码错误");
        }

    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/login";
    }













}




