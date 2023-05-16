package com.wansir.service;


import com.wansir.pojo.entity.User;
import com.wansir.utils.ResponseResult;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 13:30
 */

public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
