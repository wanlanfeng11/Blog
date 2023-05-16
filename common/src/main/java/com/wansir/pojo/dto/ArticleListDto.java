package com.wansir.pojo.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 10:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListDto {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;

    //访问量
    private Long viewCount;

    private Date createTime;
}
