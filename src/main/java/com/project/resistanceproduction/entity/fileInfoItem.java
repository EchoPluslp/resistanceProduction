package com.project.resistanceproduction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class fileInfoItem {
     private Integer equipMentId;
    private String fileName;
    private String date;
    private String fetchTime;

    private Integer type;


}
