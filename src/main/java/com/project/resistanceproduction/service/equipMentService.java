package com.project.resistanceproduction.service;

import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;

import java.util.List;

public interface equipMentService {
    public List<equipMent> getEquipMentListByPage(PageParam param);

    Integer getEquipMentListCount();
}
