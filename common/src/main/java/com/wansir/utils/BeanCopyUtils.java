package com.wansir.utils;


import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 9:04
 */
@NoArgsConstructor
public class BeanCopyUtils {
    public static <T> T copyBean(Object source, Class<T> clazz){
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <V,O> List<O> copyBeans(List<V> source, Class<O> clazz){
        List<O> res = source.stream()
                .map(t-> copyBean(t, clazz))
                .collect(Collectors.toList());
        return res;
    }
}
