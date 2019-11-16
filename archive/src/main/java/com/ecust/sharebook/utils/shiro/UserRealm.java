package com.ecust.sharebook.utils.shiro;

import com.ecust.sharebook.utils.Jwt.JwtToken;
import com.ecust.sharebook.utils.Jwt.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class UserRealm extends AuthorizingRealm {



    @Resource
    private JwtUtil jwtUtil;


    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //String username = JwtUtil.getUsername(principals.toString());
        //SysUser user = sysUserService.findByUserName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)throws AuthenticationException {
        String jwtToken = (String) token.getCredentials();
        String wxOpenId = jwtUtil.getWxOpenIdByToken(jwtToken);
        String sessionKey = jwtUtil.getSessionKeyByToken(jwtToken);
        if (StringUtils.isEmpty(wxOpenId))
            throw new AuthenticationException("user account not exits , please check your token");
        if (StringUtils.isEmpty(sessionKey))
            throw new AuthenticationException("sessionKey is invalid , please check your token");
        if (!jwtUtil.verifyToken(jwtToken))
            throw new AuthenticationException("token is invalid , please check your token");
        return new SimpleAuthenticationInfo(token, token, getName());
    }



}
