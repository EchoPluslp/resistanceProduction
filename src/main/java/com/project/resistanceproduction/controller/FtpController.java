package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.FtpUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/ftp")
public class FtpController {
    @Autowired
    private FtpUtil ftpUtils;

    /**
     */
    @RequestMapping("/test")
    public void FtpTest(HttpServletResponse response, MultipartFile file) throws IOException {

        String path = StringUtils.EMPTY;
        String name = "222.txt";
        //下载getFtpImage
        FtpUtil.downLoad(name,path,response);
      //  FtpUtil.getFtpImage(response,path);

        OutputStream out =  response.getOutputStream();

    }


}
