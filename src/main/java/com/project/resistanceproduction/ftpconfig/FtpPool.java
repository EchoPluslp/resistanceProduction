package com.project.resistanceproduction.ftpconfig;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class FtpPool {

    private static final Logger log = LoggerFactory.getLogger(FtpPool.class);

    private final GenericObjectPool<FTPClient> internalPool;


    public FtpPool(FtpClientFactory factory){
        Ftp ftp = factory.getFtpInfo();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(ftp.getMaxTotal());
        poolConfig.setMinIdle(ftp.getMinIdle());
        poolConfig.setMaxIdle(ftp.getMaxIdle());
        poolConfig.setMaxWaitMillis(ftp.getMaxWaitMillis());
        this.internalPool = new GenericObjectPool<>(factory,poolConfig);
        log.info("初始化FTPClient...");
    }


    //从连接池中取连接
    public  FTPClient getFtpClient() {
        try {
            log.info("连接FTP.....");
            return internalPool.borrowObject();
        } catch (Exception e) {
            log.error("连接FTP失败:"+e.getMessage(),e);
            return null;
        }
    }



    //将链接归还到连接池
    public  void returnFTPClient(FTPClient ftpClient) {
        try {
            internalPool.returnObject(ftpClient);
        } catch (Exception e) {
            log.error("归还到连接失败",e);
        }
    }

}