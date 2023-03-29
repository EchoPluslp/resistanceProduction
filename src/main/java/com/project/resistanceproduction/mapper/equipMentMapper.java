package com.project.resistanceproduction.mapper;

import com.project.resistanceproduction.entity.equipMent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface equipMentMapper {


    List<equipMent> getEquipMentUserInfo();

    Integer getEquipMentListCount();
}
