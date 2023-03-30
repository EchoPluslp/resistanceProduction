package com.project.resistanceproduction.service.impl;

import com.github.pagehelper.PageHelper;
import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.productLine;
import com.project.resistanceproduction.mapper.equipMentMapper;
import com.project.resistanceproduction.mapper.productLineMapper;
import com.project.resistanceproduction.service.productLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class productLineServiceImpl implements productLineService {

    @Autowired
    public productLineMapper productLineMapper;

    @Override
    public Integer getProductLineCount() {

        return productLineMapper.getProductLineCount();
    }

    @Override
    public List<productLine> getProductLineByPage(PageParam param) {
        PageHelper.startPage(param.getPageNumber(), param.getPageSize());
        List<productLine> productLineList = productLineMapper.getProductLineList();

        return productLineList;
    }
}
