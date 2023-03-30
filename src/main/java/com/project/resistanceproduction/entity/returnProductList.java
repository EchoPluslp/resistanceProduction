package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class returnProductList {
    private  Integer total;

    private List<productLine> productLineList;
}
