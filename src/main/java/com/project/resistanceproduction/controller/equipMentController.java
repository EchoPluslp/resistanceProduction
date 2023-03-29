package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.RUtils;
import com.project.resistanceproduction.Utils.Result;
import com.project.resistanceproduction.Utils.STATUS;
import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.returnListCount;
import com.project.resistanceproduction.service.equipMentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/main")
public class equipMentController {


    @Autowired
    private equipMentService equipMentServiceImpl;
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

}
