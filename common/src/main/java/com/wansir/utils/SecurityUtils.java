package com.wansir.utils;


import com.wansir.pojo.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 19:19
 */
public class SecurityUtils {

        /**
         * 获取用户
         **/
        public static LoginUser getLoginUser()
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }

        /**
         * 获取Authentication
         */
        public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

        public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && 1L == id;
    }

        public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
    }