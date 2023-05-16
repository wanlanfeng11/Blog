package com.wansir.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailDto {
    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //所属分类名
    private String categoryName;

    private Long categoryId;

    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;
    //访问量
    private Long viewCount;

    private Date createTime;
}
