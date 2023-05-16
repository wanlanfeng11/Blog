package com.wansir.pojo.dto;


import com.wansir.pojo.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 12:43
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO extends Article {
    /**
     * 标签列表
     */
    List<Long> tags;
}
