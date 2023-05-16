package com.wansir.controller;


import com.wansir.annotation.SystemLog;
import com.wansir.utils.MinioUtils;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation(value = "上传头像")
    @PostMapping("/upload")
    @SystemLog(value = "上传头像")
    public ResponseResult upload(MultipartFile img) {
        String fileName = minioUtils.upload(img);
        String url = minioUtils.getPreviewFileUrl(fileName);
        return ResponseResult.okResult(url);
    }

    @PostMapping("/getFile")
    public ResponseResult upload(String fileName) {

        String url = minioUtils.getPreviewFileUrl(fileName);
        return ResponseResult.okResult(url);
    }



}

