package com.wansir.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 9:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    //id
    private Long id;
    //标题
    private String title;

    //访问量
    private Long viewCount;
}
