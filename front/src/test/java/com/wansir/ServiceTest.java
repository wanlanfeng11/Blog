package com.wansir;


import com.wansir.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/12 12:16
 */
@SpringBootTest()
public class ServiceTest {
    @Autowired
    private TagService tagService;

    @Test
    public void list(){
        System.out.println(tagService.list().getClass());
    }
}
