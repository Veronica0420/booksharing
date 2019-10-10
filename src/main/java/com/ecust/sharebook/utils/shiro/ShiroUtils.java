package com.ecust.sharebook.utils.shiro;

import com.ecust.sharebook.pojo.Admin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroUtils {
    //类函数，共用

    /**
     * getPrincipal 获取Realm 中SimpleAuthenticationInfo info
     **/
    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static Admin getUser() {
        return (Admin)getSubjct().getPrincipal();
    }
    public static Integer getUserId() {
        return getUser().getAdminid();
    }
    public static void logout() {
        getSubjct().logout();
    }
}
