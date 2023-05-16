package com.wansir.controller;


import com.wansir.annotation.SystemLog;
import com.wansir.enums.AppHttpCodeEnum;
import com.wansir.exception.SystemException;
import com.wansir.pojo.entity.User;
import com.wansir.service.BlogLoginService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 13:27
 */
@Api("前台登录校验接口")
@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @ApiOperation("登录校验")
    @PostMapping("/login")
    @SystemLog(value = "前台登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    @SystemLog(value = "前台退出")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
