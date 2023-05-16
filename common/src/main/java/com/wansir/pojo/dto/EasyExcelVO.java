package com.wansir.pojo.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 13:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EasyExcelVO {

    @ExcelProperty("分类名")
    private String name;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("状态0：正常，1禁用")
    private String status;
}
