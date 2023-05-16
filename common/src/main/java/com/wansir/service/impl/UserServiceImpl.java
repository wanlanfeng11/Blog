package com.wansir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.enums.AppHttpCodeEnum;
import com.wansir.exception.SystemException;
import com.wansir.mapper.UserMapper;
import com.wansir.pojo.dto.*;
import com.wansir.pojo.entity.User;
import com.wansir.pojo.entity.UserRole;
import com.wansir.service.UserRoleService;
import com.wansir.service.UserService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import com.wansir.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-11 13:15:59
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoDto dto = BeanCopyUtils.copyBean(user,UserInfoDto.class);
        return ResponseResult.okResult(dto);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 查询用户
     * @param pageParameter 分页参数
     * @param userVO    用户参数
     * @return
     */
    @Override
    public PageDto<UserVO> getList(PageParameter pageParameter, UserVO userVO) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.hasText(userVO.getStatus()), User::getStatus, userVO.getStatus());
        lqw.eq(StringUtils.hasText(userVO.getPhonenumber()), User::getPhonenumber, userVO.getPhonenumber());
        lqw.like(StringUtils.hasText(userVO.getUserName()), User::getUserName, userVO.getUserName());
        Page<User> page = new Page<>(pageParameter.getPageNum(), pageParameter.getPageSize());
        page(page, lqw);
        List<UserVO> userVOS = BeanCopyUtils.copyBeans(page.getRecords(), UserVO.class);
        return new PageDto<>(userVOS, page.getTotal());
    }


    @Autowired
    private UserRoleService userRoleService;
    /**
     * 后台添加用户
     * @param addUser 用户信息
     */
    @Override
    @Transactional
    public void addUser(AddUser addUser) {
        //判断用户名是否已存在
        if(userNameExist(addUser.getUserName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //判断邮箱是否已存在
        if(emailExist(addUser.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //判断手机号是否已存在
        if(phonenumberExist(addUser.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }

        //保存到user表
        User user = BeanCopyUtils.copyBean(addUser, User.class);
        save(user);
        //保存到user_role表
        List<UserRole> userRoles = addUser.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
    }

    /**
     * 后台删除用户
     * @param id
     */
    @Override
    public void deleteUser(Long id) {
        Long userId = SecurityUtils.getUserId();
        if(userId.equals(id)){
            throw new SystemException(AppHttpCodeEnum.USER_DELETE_ERROR);
        }
        //删除user表
        removeById(id);
        //删除user_role表
        LambdaQueryWrapper<UserRole> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserRole::getUserId, id);
        userRoleService.remove(lqw);
    }

    /**
     * 根据id查找用户
     * @param id 用户id
     * @return
     */
    @Override
    public UpdateUser getUser(Long id) {
        User user = this.getById(id);
        UpdateUser updateUser = BeanCopyUtils.copyBean(user, UpdateUser.class);
        return updateUser;
    }

    /**
     * 后台更新用户信息
     * @param updateUser
     */
    @Override
    @Transactional
    public void updateUser(UpdateUser updateUser) {
        //更新user表
        User user = BeanCopyUtils.copyBean(updateUser, User.class);
        this.updateById(user);
        //更新user_role表
        //删除原始数据
        userRoleService.deleteByUserId(user.getId());
        List<UserRole> userRoles = updateUser.getRoleIds().stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        //保存新数据
        userRoleService.saveBatch(userRoles);
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail, email);
        return count(lqw) > 0 ? true : false;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getNickName, nickName);
        return count(lqw) > 0 ? true : false;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserName, userName);
        return count(lqw) > 0 ? true : false;
    }

    private boolean phonenumberExist(String phonenumber) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhonenumber, phonenumber);
        return count(lqw) > 0 ? true : false;
    }


}
