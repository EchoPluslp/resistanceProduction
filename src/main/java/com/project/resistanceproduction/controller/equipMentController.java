package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.RUtils;
import com.project.resistanceproduction.Utils.Renum;
import com.project.resistanceproduction.Utils.Result;
import com.project.resistanceproduction.Utils.STATUS;
import com.project.resistanceproduction.entity.*;
import com.project.resistanceproduction.ftpconfig.FtpConfigProperties;
import com.project.resistanceproduction.service.equipMentService;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.project.resistanceproduction.Utils.NGOK.NG;
import static com.project.resistanceproduction.Utils.NGOK.OK;

@RestController
@ResponseBody
@RequestMapping("/main")
public class equipMentController {

    @Autowired
    private equipMentService equipMentServiceImpl;

    @Autowired
    private FtpConfigProperties ftpConfigProperties;

    @Value("${filePath}")
    String filePath;

        @Value("${filePath_dataInfo}")
    String filePath_dataInfo;

    //获取主界面展示信息
    @RequestMapping("/getEquipmentList")
    public Result getestEquipMentList(@RequestBody PageParam param){

        Integer equipMentListCount = equipMentServiceImpl.getEquipMentListCount();

        List<equipMent> equipMentList = equipMentServiceImpl.getEquipMentListByPage(param);
       //根据状态id判断是设备状态
        for (equipMent equipMentIcon:
                equipMentList ) {
            if (equipMentIcon.getStatus() == STATUS.ONLINE.getCode()){
                equipMentIcon.setStatusInfo(STATUS.ONLINE.getMsg());
            }else if(equipMentIcon.getStatus() == STATUS.OFFLINE.getCode()){
                equipMentIcon.setStatusInfo(STATUS.OFFLINE.getMsg());
            }else {
                equipMentIcon.setStatusInfo(STATUS.HITCH.getMsg());
            }
        }
        returnListCount listItem = new returnListCount(equipMentListCount,equipMentList);
        return RUtils.success(listItem);
    }

    @RequestMapping("/getEquipmentInfoById")
    public Result readEquipmentInfo(Integer id){
        if (id == null) {
            return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.UNKNOWN_ERROR.getMsg());

        }
        try {


        equipMent fTPInfoStatus = equipMentServiceImpl.readEquipmentInfoByid(id);
        if (fTPInfoStatus.getFtpInfoStatus()  == 1){
            fTPInfoStatus.setFtpInfoStatusInfo("已读取");
        }else{
            fTPInfoStatus.setFtpInfoStatusInfo("未读取");
        }

        //设置在线状态
        if (fTPInfoStatus.getStatus() == STATUS.ONLINE.getCode()){
            fTPInfoStatus.setStatusInfo(STATUS.ONLINE.getMsg());
        }else if(fTPInfoStatus.getStatus() == STATUS.OFFLINE.getCode()){
            fTPInfoStatus.setStatusInfo(STATUS.OFFLINE.getMsg());
        }else {
            fTPInfoStatus.setStatusInfo(STATUS.HITCH.getMsg());
        }

        return RUtils.success(fTPInfoStatus);
        }catch (Exception e) {
            return RUtils.Err(Renum.UNKNOWN_ERROR.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        }
    }

    //ftp获取文件
    //根据创建时间获取id
    @RequestMapping("/readEquipmentInfo")
    public Result getEquipmentInfo(Integer id) throws IOException {

        if (id == null) {
            return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        }
        //通过id查找设备信息
        equipMent fTPInfoStatus = equipMentServiceImpl.readEquipmentInfoByid(id);
        if (fTPInfoStatus == null){
            return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        }
        try {

        FTPClient ftpClient = ftpConfigProperties.connectFtp();
        if(ftpClient == null){
            return RUtils.Err(Renum.CONNECT_ERROR.getCode(),Renum.CONNECT_ERROR.getMsg());
        }
        FTPFile[] files = ftpClient.listDirectories();

        //获取当前日期
        Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String currentTime = formatter.format(calendar.getTime());

        SimpleDateFormat formatterForMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //用于保存到数据库中
        List<fileInfoItem> fileInfoItems = new ArrayList<>();

        //fpt服务器主文件名字
        //根据当前日期,获取指定目录下的时间.
        for (FTPFile file: files) {

            String directoryName  =  file.getName();
           if (directoryName.contains(currentTime)){
               //进入NG文件
               String NGPath = directoryName+"/figure/NG";
               List<String>  ngFileNameList = ftpConfigProperties.getFileNameList(ftpClient,NGPath);
               for(int count = 0; count < ngFileNameList.size(); count++){
                   String NGFileName = ngFileNameList.get(count);
                   ftpConfigProperties.downloadFile(ftpClient,"/"+NGPath,NGFileName,filePath+directoryName+"/figure/NG");
                    //根据文件名获取创建的时间戳
                   FTPFile[] ftpFileArr = ftpClient.listFiles(NGPath+"/"+NGFileName);
                    if (ftpFileArr.length>0) {
                        Calendar timestamp = ftpFileArr[0].getTimestamp();
                        String createTime = formatterForMM.format(timestamp.getTime());
                        fileInfoItems.add(new fileInfoItem(id,NGPath+"/"+NGFileName,directoryName,createTime,NG.getCode()));
                    }
               }

               //进入OK文件
               String OKPath = directoryName+"/figure/OK";
               List<String>  okfileNameList = ftpConfigProperties.getFileNameList(ftpClient,OKPath);
               for(int count = 0; count < okfileNameList.size(); count++){
                   String OKFileName = okfileNameList.get(count);
                   ftpConfigProperties.downloadFile(ftpClient,"/"+OKPath,OKFileName,filePath+directoryName+"/figure/OK");
                   //根据文件名获取创建的时间戳
                   FTPFile[] ftpFileArr = ftpClient.listFiles(OKPath+"/"+OKFileName);
                   if (ftpFileArr.length>0) {
                       Calendar timestamp = ftpFileArr[0].getTimestamp();
                       String createTime = formatterForMM.format(timestamp.getTime());
                       fileInfoItems.add(new fileInfoItem(id,OKPath+"/"+OKFileName,directoryName,createTime, OK.getCode()));
                   }
               }

               //进入info文件
               String infoDataPath = directoryName+"/info";
               List<String>  infoDataList =  ftpConfigProperties.getFileNameList(ftpClient,infoDataPath);
               for(int count = 0; count < infoDataList.size(); count++){
                   String infoDataPathName = infoDataList.get(count);
                   ftpConfigProperties.downloadFile(ftpClient,"/"+infoDataPath,infoDataPathName,filePath+directoryName+filePath_dataInfo);
               }
               //插入数据
               equipMentServiceImpl.insertInfo(fileInfoItems);

               //设置已经完成17点之前的数据
               fTPInfoStatus.setFtpInfoStatus(1);

               //设置在线状态
               if (fTPInfoStatus.getStatus() == STATUS.ONLINE.getCode()){
                   fTPInfoStatus.setStatusInfo(STATUS.ONLINE.getMsg());
               }else if(fTPInfoStatus.getStatus() == STATUS.OFFLINE.getCode()){
                   fTPInfoStatus.setStatusInfo(STATUS.OFFLINE.getMsg());
               }else {
                   fTPInfoStatus.setStatusInfo(STATUS.HITCH.getMsg());
               }

                 return RUtils.success(fTPInfoStatus);
            }
          }
                return RUtils.Err(Renum.UNKNOWN_ERROR.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        }catch (Exception e){

                return RUtils.Err(Renum.UNKNOWN_ERROR.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        }
    }

    @RequestMapping("/getEquipmentImgList")
    public Result getEquipmentInfo(@RequestBody EquipmentItemInfo equipmentInfo){
        try {
            //根据设备id,良次品,起止时间,获取图片路径
            List<String> fileNameList = equipMentServiceImpl.getEquipMentInfoList(equipmentInfo);
            return RUtils.success(fileNameList);
        }catch (Exception e) {
            return  RUtils.Err(Renum.UNKNOWN_ERROR.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        }

    }


    @RequestMapping("/generateChart1")
    public Result getEquipmentStatisticalChart( @RequestBody equipmentStatisticalChart equipmentStatisticalChart) {
        //根据传过来的日期获取专属目录下的data_info
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String chooseTimeString = format.format(equipmentStatisticalChart.getChooseTime());
      // String chooseTimeString = equipmentStatisticalChart.getChooseTime();
        chooseTimeString = "data"+chooseTimeString.replace("-","");
        String infoPath = filePath+chooseTimeString+filePath_dataInfo+"//data_info.txt";
        InputStreamReader read = null;// 编码格式必须和文件的一致
        try {
            read = new InputStreamReader(new FileInputStream(infoPath), "utf-8");

            //一次读取一行
        BufferedReader bufferedReader = new BufferedReader(read);

        String lineTxt;
        equipmentValue equipmentOKValue = new equipmentValue();
        equipmentValue equipmentNGValue = new equipmentValue();

        while ((lineTxt = bufferedReader.readLine()) != null) {
            if(!lineTxt.isEmpty()){
                //判断是否含有OK数量和NG数量
                String[] splieTotalCountString =  lineTxt.split(":");

                if (splieTotalCountString[0].trim().equals("当前检测数量")){
                    //获得总数
                    Integer totalCount = Integer.valueOf(splieTotalCountString[1].trim());
                    equipmentOKValue.setCount(totalCount);
                    equipmentNGValue.setCount(totalCount);
                }
                if (splieTotalCountString[0].trim().equals("OK数量")){
                    //获得OK数量
                    Integer okCount = Integer.valueOf(splieTotalCountString[1].trim());
                    equipmentOKValue.setName("良品");
                    equipmentOKValue.setItemCount(okCount);
                    BigDecimal rateStr = new BigDecimal((float) okCount / equipmentOKValue.getCount()).setScale(3, BigDecimal.ROUND_HALF_UP);

                    equipmentOKValue.setValue(rateStr.multiply(new BigDecimal(100)));

                }
                if (splieTotalCountString[0].trim().equals("NG数量")){
                    //获得OK数量
                    Integer ngCount = Integer.valueOf(splieTotalCountString[1].trim());
                    equipmentNGValue.setName("次品");
                    equipmentNGValue.setItemCount(ngCount);
                    BigDecimal rateStr = new BigDecimal((float) ngCount / equipmentNGValue.getCount()).setScale(3, BigDecimal.ROUND_HALF_UP);

                    equipmentNGValue.setValue(rateStr.multiply(new BigDecimal(100)));

                }
            }
        }

        List<equipmentValue> list = new ArrayList<>();
        list.add(equipmentOKValue);
        list.add(equipmentNGValue);
        return RUtils.success(list);
        }catch (Exception e){
            return RUtils.Err(Renum.DATA_IS_NULL.getCode(),Renum.DATA_IS_NULL.getMsg());

        }finally {
            if (read != null) {
                try {
                    read.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
