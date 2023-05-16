package com.wansir.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 23:27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UpdateUser {

    private Long id;

    private String nickName;

    private String userName;
    private String status;

    private String sex;

    private String email;

    private List<Long> roleIds;


}