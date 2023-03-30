package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentItemInfo {
    private Integer id;
    private Integer type;
    private String startTime;
    private String endTime;
}
