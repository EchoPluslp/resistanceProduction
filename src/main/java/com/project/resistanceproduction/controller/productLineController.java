package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.RUtils;
import com.project.resistanceproduction.Utils.Renum;
import com.project.resistanceproduction.Utils.Result;
import com.project.resistanceproduction.Utils.STATUS;
import com.project.resistanceproduction.entity.*;
import com.project.resistanceproduction.service.equipMentService;
import com.project.resistanceproduction.service.productLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@ResponseBody
@RequestMapping("/main")
public class productLineController {

    @Autowired
    private productLineService productLineServiceImpl;

    @Value("${classify_bds_path}")
    private String classify_bds_path;

    @Value("${cluster_bds_path}")
    private String cluster_bds_path;

    //获取主界面展示信息
    @RequestMapping("/getProductLineList")
    public Result getestEquipMentList(@RequestBody PageParam param) {

        Integer productLineCount = productLineServiceImpl.getProductLineCount();


        List<productLine> productLineList = productLineServiceImpl.getProductLineByPage(param);
        //根据状态id判断是设备状态
        for (productLine productLineItem :
                productLineList) {
            if (productLineItem.getStatus() == STATUS.ONLINE.getCode()) {
                productLineItem.setStatusInfo(STATUS.ONLINE.getMsg());
            } else if (productLineItem.getStatus() == STATUS.OFFLINE.getCode()) {
                productLineItem.setStatusInfo(STATUS.OFFLINE.getMsg());
            } else {
                productLineItem.setStatusInfo(STATUS.HITCH.getMsg());
            }
        }

        returnProductList listItem = new returnProductList(productLineCount, productLineList);
        return RUtils.success(listItem);
    }

    @RequestMapping("/generateChart2")
    public Result generateChart() {
        try {

            //获取程序执行路径
            String path = new File("").getCanonicalPath();

            String classifyBds_path =path + "/outputdata.txt";
            File file=new File(classifyBds_path);
            //如果文件不存在,则执行matlab程序,并且等待3秒
            if (!file.exists()) {
                Process p = new ProcessBuilder(classify_bds_path).start();
                TimeUnit.SECONDS.sleep(3);
            }else{
                //判断创建日期是否是今天
                if (!checktime(classifyBds_path)){
                    //生成的outputdata创建时间不是今天,则执行matlab程序
                    Process p = new ProcessBuilder(classify_bds_path).start();
                    TimeUnit.SECONDS.sleep(3);
                }
            }

            InputStreamReader read  = new InputStreamReader(new FileInputStream(classifyBds_path), "utf-8");
            //一次读取一行
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            //总数
            Integer totalCount = 0;

            if ((lineTxt = bufferedReader.readLine()) != null) {
                if (!lineTxt.isEmpty()) {
                    //获取数据
                    String[] splitData = lineTxt.trim().split(" ");
                    for (String dataItem : splitData) {
                        totalCount+= Integer.valueOf(dataItem);
                    }
                    //以此创建参数
                    List<equipmentValue> list = new ArrayList<>();
                    //短划痕参数设置
                    BigDecimal shortScratchRateStr = new BigDecimal((float) Integer.valueOf(splitData[0]) / totalCount).setScale(3, BigDecimal.ROUND_HALF_UP);
                    shortScratchRateStr = shortScratchRateStr.multiply(new BigDecimal(100));
                    equipmentValue  shortScratch = new equipmentValue("短划痕",shortScratchRateStr,totalCount,Integer.valueOf(splitData[0]) );
                    list.add(shortScratch);

                    //长划痕参数设置
                    BigDecimal longScratchRateStr = new BigDecimal((float) Integer.valueOf(splitData[1]) / totalCount).setScale(3, BigDecimal.ROUND_HALF_UP);
                    longScratchRateStr = longScratchRateStr.multiply(new BigDecimal(100));
                    equipmentValue  longScratch = new equipmentValue("长划痕",longScratchRateStr,totalCount,Integer.valueOf(splitData[1]) );
                    list.add(longScratch);

                    //缺角参数设置
                    BigDecimal unfilledCornerRateStr = new BigDecimal((float) Integer.valueOf(splitData[2]) / totalCount).setScale(3, BigDecimal.ROUND_HALF_UP);
                    unfilledCornerRateStr = unfilledCornerRateStr.multiply(new BigDecimal(100));
                    equipmentValue  unfilledCorner = new equipmentValue("缺角",unfilledCornerRateStr,totalCount,Integer.valueOf(splitData[2]) );
                    list.add(unfilledCorner);

                    //裂纹参数设置
                    BigDecimal flawRateStr = new BigDecimal((float) Integer.valueOf(splitData[3]) / totalCount).setScale(3, BigDecimal.ROUND_HALF_UP);
                    flawRateStr = flawRateStr.multiply(new BigDecimal(100));
                    equipmentValue  flawCorner = new equipmentValue("裂纹",flawRateStr,totalCount,Integer.valueOf(splitData[3]) );
                    list.add(flawCorner);
                    return RUtils.success(list);
                }
            }
        } catch (FileNotFoundException e) {
            return RUtils.Err(Renum.DATA_IS_NULL.getCode(),Renum.DATA_IS_NULL.getMsg());
        } catch (UnsupportedEncodingException e) {
            return RUtils.Err(Renum.DATA_IS_NULL.getCode(),Renum.DATA_IS_NULL.getMsg());
        } catch (IOException e) {
            return RUtils.Err(Renum.DATA_IS_NULL.getCode(),Renum.DATA_IS_NULL.getMsg());
        } catch (InterruptedException e) {
            return RUtils.Err(Renum.DATA_IS_NULL.getCode(),Renum.DATA_IS_NULL.getMsg());
        }
        return RUtils.Err(Renum.DATA_IS_NULL.getCode(),Renum.DATA_IS_NULL.getMsg());
    }

    private boolean checktime(String  path)throws IOException {
        // 根据path获取文件的基本属性类
        BasicFileAttributes attrs = null;
            attrs = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);

        // 从基本属性类中获取文件创建时间
        FileTime fileTime = attrs.creationTime();
        // 将文件创建时间转成年月日
        long millis = fileTime.toMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date fileDate = new Date();
        fileDate.setTime(millis);
        // 毫秒转成时间字符串
        String time = dateFormat.format(fileDate);

        //获取当前年月日
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        return currentDate.equals(time);
    }

    @RequestMapping("/generateChart3")
    public Result generateChart3() {

        try {
            //调用matlab程序
            Process p = new ProcessBuilder(cluster_bds_path).start();

            //等待3秒,模拟后端实现
            TimeUnit.SECONDS.sleep(4);

            return RUtils.success(null);

        } catch (IOException e) {
            return RUtils.Err(Renum.UNKNOWN_ERROR.getCode(),Renum.UNKNOWN_ERROR.getMsg());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/generateChart4")
    public Result generateChart4() {
            return RUtils.success("12312");
    }
}
