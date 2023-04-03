package com.project.resistanceproduction.entity;

import lombok.Data;

import java.time.LocalDateTime;

//生成单日良次品统计图
@Data
public class equipmentStatisticalChart {
    private LocalDateTime chooseTime;
    private Integer id;

}
