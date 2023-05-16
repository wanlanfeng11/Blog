package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.dto.*;
import com.wansir.pojo.entity.User;
import com.wansir.utils.ResponseResult;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-05-11 13:15:59
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    PageDto<UserVO> getList(PageParameter pageParameter, UserVO userVO);

    void addUser(AddUser addUser);

    void deleteUser(Long id);

    UpdateUser getUser(Long id);

    void updateUser(UpdateUser updateUser);
}
