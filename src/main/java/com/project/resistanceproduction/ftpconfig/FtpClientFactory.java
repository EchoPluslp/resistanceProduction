package com.project.resistanceproduction.ftpconfig;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class FtpClientFactory implements PooledObjectFactory<FTPClient> {
    private static final Logger log = LoggerFactory.getLogger(FtpClientFactory.class);

    FtpClientFactory(){
        log.info("初始化FtpFactory... ");
    }

    @Resource
    private Ftp ftp;


    @Override
    public PooledObject<FTPClient> makeObject() {
        log.info("创建连接到连接池...");
        FTPClient ftpClient = new FTPClient();
        return new DefaultPooledObject<>(ftpClient);
    }



    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject)  {
        FTPClient ftpClient = pooledObject.getObject();
        try {
            ftpClient.logout();
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
            log.info("断开连接...");
        } catch (IOException e) {
            log.error("断开连接失败！");
            throw new RuntimeException("断开连接失败！", e);
        }
    }



    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftpClient = pooledObject.getObject();
        try {
            log.info("检查连接状态...");
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            return false;
        }
    }




    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient ftpClient = pooledObject.getObject();
        ftpClient.connect(ftp.getHost(),ftp.getPort());
        ftpClient.login(ftp.getUserName(), ftp.getPassWord());
        ftpClient.setControlEncoding(ftp.getEncoding());
        ftpClient.changeWorkingDirectory(ftp.getWorkDirectory());
        //设置上传文件类型为二进制，否则将无法打开文件
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        log.info("初始化连接...");
    }



    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject){
        FTPClient ftpClient = pooledObject.getObject();
        try {
            ftpClient.changeWorkingDirectory(ftp.getRoot());
            ftpClient.logout();
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
            log.info("钝化连接...");
        } catch (IOException e) {
            throw new RuntimeException("关闭连接失败!"+ e.getMessage());
        }
    }


    public Ftp getFtpInfo() {
        log.info("获取FTPPool属性...");
        return ftp;
    }

}