package com.project.resistanceproduction.mapper;

import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.equipMentItemInfo;
import com.project.resistanceproduction.entity.fileInfoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface equipMentMapper {


    List<equipMent> getEquipMentUserInfo();

    Integer getEquipMentListCount();

    equipMent readEquipmentInfoByid(@Param("id") Integer id);

    Integer insertData(@Param("fileInfoItem") fileInfoItem fileInfoItem);

    List<String> getEquipFileName(@Param("equipmentInfo") equipMentItemInfo equipmentInfo);

}
