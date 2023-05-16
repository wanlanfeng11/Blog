package com.wansir.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 14:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserLoginDto {

    private String token;
    private UserInfoDto userInfo;
}