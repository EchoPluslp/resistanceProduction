package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class equipMentItemInfo {
    private Integer equipMentId;
    private Integer type;
    private String startTime;
    private String endTime;
}
