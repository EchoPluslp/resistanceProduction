package com.project.resistanceproduction.service.impl;


import com.github.pagehelper.PageHelper;
import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.mapper.equipMentMapper;
import com.project.resistanceproduction.service.equipMentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class equipMentServiceImpl  implements equipMentService {

    @Autowired
    public equipMentMapper equipMentMapper;
    @Override
    public List<equipMent> getEquipMentListByPage(PageParam param) {
        PageHelper.startPage(param.getPageNumber(), param.getPageSize());
        List<equipMent> userMentMapper = equipMentMapper.getEquipMentUserInfo();

        return userMentMapper;
    }

    @Override
    public Integer getEquipMentListCount() {
        return equipMentMapper.getEquipMentListCount();
    }
}
