package com.wansir.controller;


import com.wansir.pojo.dto.*;
import com.wansir.pojo.entity.User;
import com.wansir.service.RoleService;
import com.wansir.service.UserService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 22:05
 */
@Api(tags = "后台用户接口")
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询用户列表")
    @GetMapping("/list")
    public ResponseResult getList(PageParameter pageParameter
                                , UserVO userVO){
        PageDto<UserVO> pageDto = userService.getList(pageParameter, userVO);
        return ResponseResult.okResult(pageDto);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping()
    public ResponseResult addUser(@RequestBody AddUser addUser){
        userService.addUser(addUser);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "查询用户")
    @GetMapping("/{id}")
    public ResponseResult getUser(@PathVariable("id") Long id){

        UpdateUser updateUser = userService.getUser(id);
        List<RoleVO> roles = roleService.getRolesByStatus();
        List<Long> roleIds = roleService.getRoleIds(id);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("user", updateUser);
        map.put("roles", roles);
        map.put("roleIds", roleIds);
        return ResponseResult.okResult(map);
    }

    @ApiOperation(value = "更新用户")
    @PutMapping()
    public ResponseResult updateUser(@RequestBody UpdateUser updateUser){

        userService.updateUser(updateUser);
        return ResponseResult.okResult();
    }
}
