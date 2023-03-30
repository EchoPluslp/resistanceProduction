package com.project.resistanceproduction.mapper;

import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.productLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface productLineMapper {


    List<productLine> getProductLineList();

    Integer getProductLineCount();
}
