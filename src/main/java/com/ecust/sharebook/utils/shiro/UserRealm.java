package com.ecust.sharebook.utils.shiro;

import com.ecust.sharebook.mapper.AdminMapper;
import com.ecust.sharebook.pojo.Admin;
import com.ecust.sharebook.utils.R;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    AdminMapper adminMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        // 从 principals获取主身份信息
       // Integer userId = ShiroUtils.getUserId();
       // Set<String> perms = menuService.listPerms(userId);
       // SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 将上边查询到授权信息填充到simpleAuthorizationInfo对象中
      //  info.setStringPermissions(perms);
      //  return info;
        return  null;

    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("adminName", username);
        String password = new String((char[]) token.getCredentials());

        R.ok("realm");
        // 查询用户信息
        Admin admin = adminMapper.list(map);

        // 账号不存在
        if (admin == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        // 密码错误
        if (!password.equals(admin.getPasswd())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(admin, password, getName());
        return info;
    }
}
