package com.wansir.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.mapper.LogMapper;
import com.wansir.pojo.entity.Log;
import com.wansir.service.LogService;
import org.springframework.stereotype.Service;

/**
 * 系统日志(Log)表服务实现类
 *
 * @author makejava
 * @since 2023-05-15 21:34:40
 */
@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
