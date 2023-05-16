package com.wansir.pojo.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 11:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {

    @TableId
    private Long id;


    private String name;

    private String logo;

    private String description;

    private String status;

    //网站地址
    private String address;

}
