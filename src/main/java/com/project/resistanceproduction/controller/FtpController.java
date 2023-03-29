package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/ftp")
public class FtpController {
    @Autowired
    private FtpUtil ftpUtils;

    /**
     * 读取ftp上的pdf文件
     */
    @RequestMapping("/test")
    public void FtpTest(HttpServletResponse response, MultipartFile file) throws IOException {
        //FileInputStream inputStream = file.getInputStream();
        //String fileName = file.getOriginalFilename();
        FileInputStream inputStream = new FileInputStream(new File("D://MyDrivers//123.txt"));
        //上传
        String path = "txt";
        String fileName = FtpUtil.upload(inputStream, "22.txt",path);

        System.out.println(fileName);

        //下载
        FtpUtil.downLoad(fileName,path,response);
    }


}
