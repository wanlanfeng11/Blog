package com.wansir.service.impl;


import com.wansir.enums.SystemConstants;
import com.wansir.pojo.dto.BlogUserLoginDto;
import com.wansir.pojo.dto.UserInfoDto;
import com.wansir.pojo.entity.LoginUser;
import com.wansir.pojo.entity.User;
import com.wansir.service.BlogLoginService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.JwtUtil;
import com.wansir.utils.RedisCache;
import com.wansir.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 13:30
 */
@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);

        //把token和userinfo封装 返回
        //把User转换成UserInfoDto
        UserInfoDto userInfoDto = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoDto.class);
        BlogUserLoginDto dto = new BlogUserLoginDto(jwt,userInfoDto);
        return ResponseResult.okResult(dto);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.REDIS_LOGIN_KEY_PREFIX +userId);
        return ResponseResult.okResult();
    }
}
