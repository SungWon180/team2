package com.team2.dbbuddy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DataTypeDTO {
    private Integer typeId;
    private String typeNm;
    private LocalDateTime regDt;
}
