package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.dto.LinkDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.PageParameter;
import com.wansir.pojo.entity.Link;
import com.wansir.utils.ResponseResult;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-05-11 11:05:49
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageDto<LinkDto> listAll(PageParameter pageParameter, String name, String status);

    void deleteByIds(String ids);
}
