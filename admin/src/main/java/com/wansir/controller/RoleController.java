package com.wansir.controller;


import com.wansir.pojo.dto.*;
import com.wansir.pojo.entity.Role;
import com.wansir.service.RoleService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 17:38
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询角色列表")
    @GetMapping("/list")
    public ResponseResult getList(Integer pageNum, Integer pageSize, String roleName, String status){
        PageDto pageVO = roleService.getList(pageNum, pageSize, roleName, status);
        return ResponseResult.okResult(pageVO);
    }

    @ApiOperation(value = "修改角色状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatusById(@RequestBody RoleDto roleDto){
        roleService.changeStatus(roleDto);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id") Long id){
        roleService.deleteRole(id);
        return ResponseResult.okResult();
    }


    @ApiOperation(value = "新增角色")
    @PostMapping()
    public ResponseResult addRole(@RequestBody AddRoleDto role){
        roleService.addRole(role);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "查询角色")
    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable("id") Long id){
        RoleVO roleVO = roleService.getRole(id);
        return ResponseResult.okResult(roleVO);
    }

    @ApiOperation(value = "更新角色")
    @PutMapping()
    public ResponseResult updateRole(@RequestBody AddRoleDto addRoleDto){
        roleService.updateRole(addRoleDto);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "查询正常状态的角色")
    @GetMapping("/listAllRole")
    public ResponseResult getRolesByStatus(){
        List<RoleVO> roleVOS = roleService.getRolesByStatus();
        return ResponseResult.okResult(roleVOS);
    }
}
