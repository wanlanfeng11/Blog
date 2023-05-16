package com.wansir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.enums.SystemConstants;
import com.wansir.mapper.LinkMapper;
import com.wansir.pojo.dto.LinkDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.PageParameter;
import com.wansir.pojo.entity.Link;
import com.wansir.service.LinkService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-05-11 11:05:50
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkDto> LinkDtos = BeanCopyUtils.copyBeans(links, LinkDto.class);
        //封装返回
        return ResponseResult.okResult(LinkDtos);
    }

    /**
     * 后台查询友链列表
     *
     * @param pageParameter
     * @param name
     * @param status
     * @return
     */
    @Override
    public PageDto<LinkDto> listAll(PageParameter pageParameter, String name, String status) {
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(name), Link::getName, name);
        lqw.eq(StringUtils.hasText(status), Link::getStatus, status);
        Page<Link> linkPage = new Page<>(pageParameter.getPageNum(), pageParameter.getPageSize());
        page(linkPage, lqw);
        List<LinkDto> linkDtos = BeanCopyUtils.copyBeans(linkPage.getRecords(), LinkDto.class);
        return new PageDto<LinkDto>(linkDtos, linkPage.getTotal());
    }

    /**
     * 删除多个友链
     * @param ids 友链集合
     */
    @Override
    @Transactional
    public void deleteByIds(String ids) {
        String[] list = ids.split(",");
        List<String> strings = Arrays.asList(list);
        removeByIds(strings);
    }
}
