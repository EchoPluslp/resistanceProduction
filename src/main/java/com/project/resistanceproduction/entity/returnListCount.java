package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class returnListCount {
    private  Integer total;
    private List<equipMent> testEquipmentList;
}
