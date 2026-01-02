package com.team2.dbbuddy.dto;

import lombok.Data;
import java.util.List;

@Data
public class TableCreationRequestDTO {
    private TableMetaDTO table;
    private List<ColumnMetaDTO> columns;
}
