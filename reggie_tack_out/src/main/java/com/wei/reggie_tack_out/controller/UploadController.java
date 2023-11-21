package com.wei.reggie_tack_out.controller;

import com.wei.reggie_tack_out.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*
* 文件上传和下载
* */

@Slf4j
@RestController
@RequestMapping("/common")
public class UploadController {

    // 通过 @Value("${reggie.path}") 读取到 application.yml 配置文件中的动态转存位置
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        //file是个临时文件，我们在断点调试的时候可以看到，但是执行完整个方法之后就消失了
        log.info("获取文件{}", file.toString());
//        //测试上传文件能否成功
//        try {
//            file.transferTo(new File("E://test.jpg"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        // 判断当前目录是否存在，不存在则创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 获取传入的原文件名
        String originalFilename = file.getOriginalFilename();
        // 获取一下格式的后缀，取子串，起始点为最后一个
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 为了防止重复的文件名，我们需要使用 UUID
        String fileName = UUID.randomUUID() + suffix;
        // 将其转存到指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 将文件名返回给前端，便于后期开发
        return Result.success(fileName);
    }

    /* 文件下载 */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        FileInputStream fis = null;
        ServletOutputStream os = null;

        try {
            fis = new FileInputStream(basePath + name);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            os = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while (true) {
                if (!((len = fis.read(buffer)) != -1)) {
                    break;
                }
                os.write(buffer, 0, len);
            }
            fis.close();
            os.close();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }


    }
}
