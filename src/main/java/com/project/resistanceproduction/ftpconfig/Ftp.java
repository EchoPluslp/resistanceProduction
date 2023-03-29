package com.project.resistanceproduction.ftpconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//@Data 编译时能自动生成get set 方法
//@Component Ftp类交给spring 管理
//@Value("${}") 获取配置文件的属性
@Data
@Component
public class Ftp {
    /**
     * FTP地址
     **/
    @Value("${ftp.host}")
    private String host;
    /**
     * FTP端口
     **/
    @Value("${ftp.port}")
    private int port ;
    /**
     * FTP用户名
     **/
    @Value("${ftp.userName}")
    private String userName;
    /**
     * FTP密码
     **/
    @Value("${ftp.passWord}")
    private String passWord;

    /**
     * 目录
     **/
    @Value("${ftp.workDirectory}")
    private String workDirectory;


    /**
     * FTP根目录
     **/
    @Value("${ftp.root}")
    private String root;

    /**
     * 编码
     **/
    @Value("${ftp.encoding}")
    private String encoding;

    /**
     * 最多连接数
     **/
    @Value("${ftp.maxTotal}")
    private int maxTotal;

    @Value("${ftp.minIdle}")
    private int minIdle;

    @Value("${ftp.maxIdle}")
    private int maxIdle;

    @Value("${ftp.maxWaitMillis}")
    private int maxWaitMillis;
}