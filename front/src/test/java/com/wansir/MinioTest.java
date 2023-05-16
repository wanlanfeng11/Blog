package com.wansir;


import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/13 17:29
 */
@SpringBootTest
public class MinioTest {
    @Autowired
    private MinioClient minioClient;

    @Test
    void testUpload(){

    }
}
