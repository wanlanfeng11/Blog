package com.wansir.controller;


import com.wansir.enums.AppHttpCodeEnum;
import com.wansir.exception.SystemException;
import com.wansir.pojo.dto.AdminUserInfoDto;
import com.wansir.pojo.dto.RoutersDto;
import com.wansir.pojo.dto.UserInfoDto;
import com.wansir.pojo.entity.LoginUser;
import com.wansir.pojo.entity.User;
import com.wansir.service.AdminLoginService;
import com.wansir.service.MenuService;
import com.wansir.service.RoleService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import com.wansir.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/12 13:53
 */
@RestController
@Api(tags = "后台用户接口")
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "登录")
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoDto> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoDto userInfoDto = BeanCopyUtils.copyBean(user, UserInfoDto.class);
        //封装数据返回

        AdminUserInfoDto adminUserInfoDto = new AdminUserInfoDto(perms,roleKeyList,userInfoDto);
        return ResponseResult.okResult(adminUserInfoDto);
    }


    @ApiOperation(value = "获取菜单数据")
    @GetMapping("getRouters")
    public ResponseResult<RoutersDto> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<RoutersDto> menus = menuService.selectRouterMenuTreeByUserId(userId);

        //封装数据返回
        Map<String, List<RoutersDto>> map = new HashMap<>();
        map.put("menus", menus);
        return ResponseResult.okResult(map);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }

}
