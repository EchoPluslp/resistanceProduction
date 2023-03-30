package com.project.resistanceproduction.service;

import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.productLine;

import java.util.List;

public interface productLineService {
    Integer getProductLineCount();

    List<productLine> getProductLineByPage(PageParam param);
}
