package com.wansir.pojo.dto;


import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 22:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParameter {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页显示的数量
     */
    private Integer pageSize;
}
