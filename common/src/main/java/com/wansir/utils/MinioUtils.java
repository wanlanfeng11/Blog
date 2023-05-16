package com.wansir.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description： minio工具类
 * @version：3.0
 */
@Component
public class MinioUtils {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;


    /**
     * description: 判断bucket是否存在，不存在则创建
     *
     * @return: void
     */
    public void existBucket(String name) {
        BucketExistsArgs build = BucketExistsArgs.builder().bucket(name).build();
        try {
            boolean exists = minioClient.bucketExists(build);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        MakeBucketArgs build = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        try {
            minioClient.makeBucket(build);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        RemoveBucketArgs build = RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build();
        try {
            minioClient.removeBucket(build);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return 返回预览地址
     */
    public String upload(MultipartFile file) {
        //获取源文件名
        String fileName = file.getOriginalFilename();
        //判断是否合法
        if (!StringUtils.hasText(fileName)) {
            throw new RuntimeException();
        }
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //使用UUID生成文件名
        String objectName = UUID.randomUUID().toString();
        //组合文件名
        objectName = objectName + suffix;
        //获取文件流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //构建文件对象
        PutObjectArgs build = PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(inputStream, file.getSize(), -1).build();
        try {
            minioClient.putObject(build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return objectName;

    }

    /**
     * 返回文件的url
     *
     * @param fileName 文件名
     * @return 文件url
     */
    public String getPreviewFileUrl(String fileName)  {
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("response-content-type", "application/json");

        String url =
                null;
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(2, TimeUnit.HOURS)
                            .extraQueryParams(reqParams)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return url.substring(0, url.indexOf("?"));
    }



    /**
     *
     * @param fileName w文件名
     * @param response  请求响应体
     * @return
     */
    public void download(String fileName, HttpServletResponse response) {
        GetObjectArgs build = GetObjectArgs.builder().bucket(bucketName).object(fileName).build();
        try (GetObjectResponse object = minioClient.getObject(build);
             ServletOutputStream os = response.getOutputStream()
        ) {
            byte[] bytes = new byte[1024];
            int len;
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            while ((len = object.read()) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 查看文件对象
     * @param bucketName 存储bucket名称
     * @return 存储bucket内文件对象信息
     */


    /**
     * 删除文件
     *
     * @param bucketName 存储bucket名称
     * @param fileName   文件名
     */
    public void removeObjects(String bucketName, String fileName) {
        //构建删除对象
        RemoveObjectArgs build = RemoveObjectArgs.builder().bucket(bucketName).build();
        try {
            //删除
            minioClient.removeObject(build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

