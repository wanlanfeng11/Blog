package com.wansir.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 10:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private List<T> rows;
    private Long total;
}
