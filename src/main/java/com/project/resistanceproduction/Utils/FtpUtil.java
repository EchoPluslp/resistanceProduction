package com.project.resistanceproduction.Utils;

import com.project.resistanceproduction.ftpconfig.Ftp;
import com.project.resistanceproduction.ftpconfig.FtpPool;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Component
public class FtpUtil {

    private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

    @Resource
    private FtpPool rePool;
    @Resource
    private Ftp reFtp;


    private static FtpPool pool;

    private static Ftp ftp;


    //PostConstruct 用在依赖关系注入完成之后需要执行的方法上，以执行任何初始化
    @PostConstruct
    public void init() {
        pool = rePool;
        ftp = reFtp;
        log.info("初始化FTP完成...");
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param inputStream 上传到FTP服务器上的文件流
     * @param fileName    上传到FTP服务器上的文件名
     * @return 成功返回文件名，否则返回null
     */
    public static String upload(FileInputStream inputStream, String fileName,String path){
        //获取 最后的小数点以及后面的字符串
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        FTPClient ftpClient = pool.getFtpClient();

        String newFileName = null;
        if (ftpClient != null) {
            //开始进行文件上传
            newFileName = UUID.randomUUID() + suffix;
            try {
                //修改文件路径
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setControlEncoding("UTF-8");



                //执行文件传输
                if (!ftpClient.storeFile(newFileName, inputStream)) {//上传失败
                    throw new RuntimeException("上传失败");
                }
                log.info("上传成功！");
            } catch (Exception e) {
                log.error("上传失败:" + e.getMessage(), e);
                return null;
            } finally {//关闭资源
                log.info("开始归还连接");
                pool.returnFTPClient(ftpClient);
            }
        }
        return newFileName;
    }

    /**
     * Description: 下载文件
     *
     * @param fileName FTP服务器中的文件名
     * @param resp     响应客户的响应体
     */
    public static void downLoad(String fileName,String path, HttpServletResponse resp) {
        FTPClient ftpClient = pool.getFtpClient();
        if (ftpClient != null) {
            resp.setContentType("multipart/form-data");
            resp.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            //将文件直接读取到响应体中
            OutputStream out = null;
            try {
                out = resp.getOutputStream();
                //路径为空时取配置文件的路径
                String newPath = StringUtils.isBlank(path) ? ftp.getWorkDirectory():path;
                String conPath = newPath + "/" + fileName;
                ftpClient.retrieveFile( conPath, out);
            } catch (IOException e) {
                log.error("下载失败：");
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        log.error("关闭FTP流失败：" + e.getMessage());
                    }
                }
            }
            //归还连接
            pool.returnFTPClient(ftpClient);
        }
    }

    /**
     * Description: 获取图片
     */
    public static void getFtpImage(HttpServletResponse response,String filepath) {
        //创建FTP连接
        FTPClient ftp;
        OutputStream outStream;
        InputStream in = null;
        try {
            ftp = pool.getFtpClient();
            //获取文件流
            in = ftp.retrieveFileStream(filepath);
            if(in != null) {
                //获取最后小数点的往后的的字符
                String picType = filepath.substring(filepath.lastIndexOf(".") + 1);
                BufferedImage bufImg = ImageIO.read(in);
                outStream = response.getOutputStream();
                ImageIO.write(bufImg, picType, outStream);
                log.info("获取图片成功");
            }else {
                throw new RuntimeException("文件不存在！");
            }
        } catch (IOException e) {
            log.error("获取图片失败", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
    }





    /**
     * 删除多个文件
     */
    private Integer deleteFiles(String path, List<String> files) {
        int count = 0;
        FTPClient ftpClient;
        try {
            ftpClient = pool.getFtpClient();
            ftpClient.changeWorkingDirectory(path);
            for (String file : files) {
                if (ftpClient.deleteFile(file)) {
                    log.info("文件 {} 删除成功", file);
                    count++;
                } else {
                    log.info("文件 {} 删除失败", file);
                }
            }
            log.info("共删除 {} 个文件", count);
        } catch (IOException e) {
            log.error("连接FTP服务器失败");
        }
        return count;
    }

}