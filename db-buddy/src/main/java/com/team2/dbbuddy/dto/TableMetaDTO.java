package com.team2.dbbuddy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TableMetaDTO {
    private Integer tblId;
    private String tblNm;
    private String tblDesc;
    private Integer userId;
    private LocalDateTime regDt;
}
