package com.team2.dbbuddy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ColumnMetaDTO {
    private Integer colId;
    private Integer tblId;
    private String colNm;
    private Integer typeId;
    private Integer typeLength;
    private String isNullable;
    private String isPk;
    private String isAutoIncrement;
    private Integer orderNo;
    private LocalDateTime regDt;

    // For joining with DATA_TYPES
    private String typeNm;
}
