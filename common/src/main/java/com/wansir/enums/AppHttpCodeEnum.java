package com.wansir.enums;


import lombok.Data;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/10 22:52
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(505, "评论不能为空"),



    USERNAME_NOT_NULL(506, "用户名不能为空"),
    PASSWORD_NOT_NULL(507, "密码不能为空"),
    EMAIL_NOT_NULL(507, "邮箱不能为空"),
    NICKNAME_NOT_NULL(508, "昵称不能为空"),
    NICKNAME_EXIST(509, "昵称已存在"),
    MENU_ID_ERRO(500, "上级菜单不能选择自己"),
    HAVE_MENU(500, "当前菜单还有子菜单"),
    USER_DELETE_ERROR(500, "不能删除当前用户");

    private int code;
    private String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
