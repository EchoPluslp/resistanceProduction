package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.ftpconfig.FtpConfigProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/ftp")
public class FtpController {
    @Autowired
    private FtpConfigProperties ftpConfigProperties;

    @RequestMapping("/downFtpFile")
    public void downFtpFile(){
        FTPClient ftpClient = ftpConfigProperties.connectFtp();
        // 路径必须是对应登录账号访问权限的路径  否则会识别不到文件
        // useradd -d /etc/vsftpd/data xiangjiao  创建账户 xiangjiao，并设置其访问的ftp服务路径为 /etc/vsftpd/data
        //  passwd xiangjiao  给账户  xiangjiao 设置密码
        ftpConfigProperties.downloadFile(ftpClient,"/info","222.txt","E:\\JAVA_WORKSPACE\\ftpWorkDir");
    }

    @RequestMapping("/uploadFtpFile")
    public void uploadFtpFile(){
        FTPClient ftpClient = ftpConfigProperties.connectFtp();
        ftpConfigProperties.uploadFile(ftpClient,"/etc/vsftpd/data","2233.txt","E:\\study\\springboot-stydy\\doc\\22.txt");
    }


}
