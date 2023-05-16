package com.wansir.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 18:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long roleId;
    private String status;
}
