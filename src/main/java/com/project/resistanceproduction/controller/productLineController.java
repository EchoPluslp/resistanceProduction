package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.RUtils;
import com.project.resistanceproduction.Utils.Result;
import com.project.resistanceproduction.Utils.STATUS;
import com.project.resistanceproduction.entity.*;
import com.project.resistanceproduction.service.equipMentService;
import com.project.resistanceproduction.service.productLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/main")
public class productLineController {

    @Autowired
    private productLineService productLineServiceImpl;
    //获取主界面展示信息
    @RequestMapping("/getProductLineList")
    public Result getestEquipMentList(@RequestBody PageParam param){

        Integer productLineCount = productLineServiceImpl.getProductLineCount();


        List<productLine> productLineList = productLineServiceImpl.getProductLineByPage(param);
        //根据状态id判断是设备状态
        for (productLine productLineItem:
                productLineList ) {
            if (productLineItem.getStatus() == STATUS.ONLINE.getCode()){
                productLineItem.setStatusInfo(STATUS.ONLINE.getMsg());
            }else if(productLineItem.getStatus() == STATUS.OFFLINE.getCode()){
                productLineItem.setStatusInfo(STATUS.OFFLINE.getMsg());
            }else {
                productLineItem.setStatusInfo(STATUS.HITCH.getMsg());
            }
        }

        returnProductList listItem = new returnProductList(productLineCount,productLineList);
        return RUtils.success(listItem);
    }
}
