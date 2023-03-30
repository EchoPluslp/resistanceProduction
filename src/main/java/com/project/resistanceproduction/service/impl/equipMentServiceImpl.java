package com.project.resistanceproduction.service.impl;


import com.github.pagehelper.PageHelper;
import com.project.resistanceproduction.entity.EquipmentItemInfo;
import com.project.resistanceproduction.entity.PageParam;
import com.project.resistanceproduction.entity.equipMent;
import com.project.resistanceproduction.entity.fileInfoItem;
import com.project.resistanceproduction.mapper.equipMentMapper;
import com.project.resistanceproduction.service.equipMentService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class equipMentServiceImpl  implements equipMentService {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

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

    @Override
    public equipMent readEquipmentInfoByid(Integer id) {
        return equipMentMapper.readEquipmentInfoByid(id);
    }

    @Override
    public void insertInfo(List<fileInfoItem> fileInfoItems) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);

        try {
            equipMentMapper insertMapper = session.getMapper(equipMentMapper.class);

            for (fileInfoItem dateitem : fileInfoItems) {
                insertMapper.insertData(dateitem);
            }

            session.commit();
            session.clearCache();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<String> getEquipMentInfoList(EquipmentItemInfo equipmentInfo) {
       return equipMentMapper.getEquipFileName(equipmentInfo);
    }
}
