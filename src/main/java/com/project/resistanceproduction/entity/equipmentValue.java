package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//良次品统计图
@Data
@AllArgsConstructor
@NoArgsConstructor
public class equipmentValue {
    //良品还是次品
    private String name;
    //百分比
    private BigDecimal value;

    //总数
    private Integer count;

    //单项数量
    private Integer itemCount;
}
