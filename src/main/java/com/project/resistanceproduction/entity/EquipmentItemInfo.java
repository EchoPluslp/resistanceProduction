package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentItemInfo {
    private Integer id;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
