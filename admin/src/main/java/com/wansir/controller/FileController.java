package com.wansir.controller;


import com.wansir.service.CategoryService;
import com.wansir.utils.MinioUtils;
import com.wansir.utils.ResponseResult;
import com.wansir.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/13 17:33
 */


@Api(tags = "文件相关接口")
@RestController
public class FileController {


    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile img) {
        String fileName = minioUtils.upload(img);
        String url = minioUtils.getPreviewFileUrl(fileName);
        return ResponseResult.okResult(url);
    }

    @GetMapping("/getFile")
    public ResponseResult getURLByFileName(String fileName) {
        String url = minioUtils.getPreviewFileUrl(fileName);
        return ResponseResult.okResult(url);
    }



}

