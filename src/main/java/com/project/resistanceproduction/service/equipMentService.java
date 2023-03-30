package com.project.resistanceproduction.service;

import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.fileInfoItem;

import java.util.List;

public interface equipMentService {
    public List<equipMent> getEquipMentListByPage(PageParam param);

    Integer getEquipMentListCount();

    Integer readEquipmentInfoByid(Integer id);


    void insertInfo(List<fileInfoItem> fileInfoItems);
}
