package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.dto.AddRoleDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.RoleDto;
import com.wansir.pojo.dto.RoleVO;
import com.wansir.pojo.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-05-12 19:54:27
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    PageDto getList(Integer pageNum, Integer pageSize, String roleName, String status);

    void changeStatus(RoleDto roleDto);

    void addRole(AddRoleDto role);

    void deleteRole(Long id);

    RoleVO getRole(Long id);

    void updateRole(AddRoleDto addRoleDto);

    List<RoleVO> getRolesByStatus();

   List<Long> getRoleIds(Long id);
}
