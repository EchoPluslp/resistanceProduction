package com.project.resistanceproduction.ftpconfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;



@Data
@Component
public class FtpConfigProperties {
    // 连接超时时间设置
    private static final int timeout = 1000*30;
    // ftp 服务地址
    private String host = "192.168.1.108";
    // ftp 服务账户
    private String userName = "admin";
    // ftp 密码
    private String pwd = "admin";
    // 端口 ftp 默认 21 ，登录端口。20为传输端口，此处使用连接端口
    private Integer port = 21 ;
    // 目录
    private String path;

    /**
     * 创建连接
     * @return
     */
    public FTPClient connectFtp() {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.setConnectTimeout(timeout);
            ftpClient.connect(host,port);
            ftpClient.login(userName,pwd);
            ftpClient.setControlEncoding("UTF-8"); //
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); // 二进制
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                throw new Exception("连接FTP失败，用户名或密码错误！");
            } else {
                System.out.println("FTP连接成功");
            }
        }catch (Exception e){
            return null;
        }

        return ftpClient;
    }

    /**
     * 关闭FTP连接
     *
     * @param ftpClient
     */
    public void closeFtpClient(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                System.out.println("登陆FTP失败，请检查FTP相关配置信息是否正确！"+e.getMessage());

            }
        }
    }

    /**
     * 按前后缀查询文件
     * @param facepic
     * @param prefix
     * @param suffix
     * @return
     */
    public List<String> showPatternFiles(String facepic, String prefix, String suffix) {
        List<String> ret = new ArrayList();
        String fileName = null;
        FTPClient ftpClient = null;
        try {
            ftpClient = connectFtp();
            boolean changeFlag = ftpClient.changeWorkingDirectory(facepic);
            if (!changeFlag) {
                throw new IOException("进入Ftp目录" + facepic + "失败");
            }
            FTPFile[] files = ftpClient.listFiles(getPath());
            for (FTPFile ftpFile : files) {
                fileName = ftpFile.getName();
                if ((!".".equals(fileName)) && (!"..".equals(fileName))) {
                    String regEx = null;
                    if (StringUtils.isNotBlank(prefix)) {
                        regEx = prefix + "*." + suffix;
                    } else {
                        regEx = suffix + "$";
                    }
                    Pattern pattern = Pattern.compile(regEx);
                    if (pattern.matcher(fileName).find()) {
                        ret.add(fileName);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("获取文件失败获取文件失败"+e.getMessage());

        } finally {
            if (ftpClient != null) {
                //close(ftpClient);
            }
        }
        return ret;
    }




    /**
     * 从FTP下载文件到本地
     *
     */
    public String downloadFile(FTPClient ftpClient, String remotePath, String fileName, String downloadPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        final File targetFile = new File(downloadPath + File.separator + fileName);
        try {
            is = ftpClient.retrieveFileStream(remotePath+"/"+ fileName);// 获取ftp上的文件

            if(!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(new File(downloadPath+File.separator+fileName));
            // 文件读取方式一
            int i;
            byte[] bytes = new byte[102400];
            while ((i = is.read(bytes)) != -1) {
                fos.write(bytes, 0, i);
            }
            // 文件读取方式二
            //ftpClient.retrieveFile(fileName, new FileOutputStream(new File(downloadPath)));
            ftpClient.completePendingCommand();
            System.out.println("FTP文件下载成功");

        } catch (Exception e) {
            System.out.println("FTP文件下载失败");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                System.out.println("下载流关闭失败");

            }
        }
        return targetFile.getAbsolutePath();
    }


    /**
     * 上传文件
     *
     * @param serviceDec     ftp服务保存地址
     * @param fileName       上传到ftp的文件名
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return
     */
    public boolean uploadFile(FTPClient ftpClient, String serviceDec, String fileName, String originfilename) {
        System.out.println("开始上传文件");

        try (InputStream input = new FileInputStream(new File(originfilename))) {
            return uploadFile(ftpClient, serviceDec, fileName, input);
        } catch (FileNotFoundException e) {
            System.out.println("文件上传失败"+e.getMessage());

        } catch (IOException e) {
            System.out.println("文件上传失败"+e.getMessage());

        }
        return false;
    }

    /**
     * 上传文件
     *
     * @param serviceDec  ftp服务保存地址
     * @param fileName    上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    private boolean uploadFile(FTPClient ftpClient, String serviceDec, String fileName, InputStream inputStream) {
        try {
            System.out.println("开始上传文件");
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            createDirecroty(ftpClient, serviceDec);
            ftpClient.makeDirectory(serviceDec);
            ftpClient.changeWorkingDirectory(serviceDec);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();

            ftpClient.logout();
            System.out.println("上传文件成功");

        } catch (Exception e) {
            System.out.println("上传文件失败");

        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println("上传文件失败");

                return false;
            }
        }
        return true;
    }


    //改变目录路径
    private boolean changeWorkingDirectory(FTPClient ftpClient, String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                System.out.println("上传文件"+directory+"成功");


            } else {
                System.out.println("进入文件夹"+directory+"失败！开始创建文件夹");

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    private boolean createDirecroty(FTPClient ftpClient, String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(ftpClient, new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(ftpClient, path)) {
                    if (makeDirectory(ftpClient, subDirectory)) {
                        changeWorkingDirectory(ftpClient, subDirectory);
                    } else {
                        System.out.println("创建目录[" + subDirectory + "]失败");

                        changeWorkingDirectory(ftpClient, subDirectory);
                    }
                } else {
                    changeWorkingDirectory(ftpClient, subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    //判断ftp服务器文件是否存在
    private boolean existFile(FTPClient ftpClient, String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    //创建目录
    private boolean makeDirectory(FTPClient ftpClient, String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                System.out.println("创建文件夹" + dir + " 成功！");


            } else {
                System.out.println("创建文件夹" + dir + "失败！");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取FTP某一特定目录下的所有文件名称
     *
     * @param ftpClient  已经登陆成功的FTPClient
     * @param ftpDirPath FTP上的目标文件路径
     */
    public List<String> getFileNameList(FTPClient ftpClient, String ftpDirPath) {
        List<String> list = new ArrayList();
        try {

            // 通过提供的文件路径获取FTPFile对象列表
            FTPFile[] files = ftpClient.listFiles(ftpDirPath);
            // 遍历文件列表，打印出文件名称
            for (int i = 0; i < files.length; i++) {
                FTPFile ftpFile = files[i];
                // 此处只打印文件，未遍历子目录（如果需要遍历，加上递归逻辑即可）
                if (ftpFile.isFile()) {
//                        log.info(ftpDirPath + ftpFile.getName());
                    list.add(ftpFile.getName());
                }
            }
        } catch (IOException e) {
            System.out.println("错误" +e.getMessage());

        }
        return list;
    }

    /**
     * 获取到服务器文件夹里面最新创建的文件名称
     *
     * @param ftpDirPath 文件路径
     * @param ftpClient  ftp的连接
     * @return fileName
     */
    public String getNewFile(FTPClient ftpClient, String ftpDirPath) throws Exception {

        // 通过提供的文件路径获取FTPFile对象列表
        FTPFile[] files = ftpClient.listFiles(ftpDirPath);
        if (files == null) {
            throw new Exception("文件数组为空");
        }
        Arrays.sort(files, new Comparator<FTPFile>() {
            @Override
            public int compare(FTPFile f1, FTPFile f2) {
                return f1.getTimestamp().compareTo(f2.getTimestamp());
            }

            public boolean equals(Object obj) {
                return true;
            }
        });
        return ftpDirPath + "/" + files[files.length - 1].getName();

    }
}

