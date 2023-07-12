package com.imooc.controller;

import com.imooc.MinIOConfig;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.MinIOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Api(tags = "FileController这是一个文件上测试的接口")
@RestController
public class FileController {
    @Autowired
    private MinIOConfig minIOConfig;
    @PostMapping("upload")
    public GraceJSONResult upload(MultipartFile file) throws Exception{
        String filename = file.getOriginalFilename();
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                filename,file.getInputStream());

        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + filename;
        return GraceJSONResult.ok(imgUrl);
    }
}
