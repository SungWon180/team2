package com.team2.dbbuddy.service;

import com.team2.dbbuddy.dto.ColumnMetaDTO;
import com.team2.dbbuddy.dto.DataTypeDTO;
import com.team2.dbbuddy.dto.TableMetaDTO;
import com.team2.dbbuddy.mapper.MetaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaMapper metaMapper;

    @Transactional
    public void createTableWithColumns(TableMetaDTO tableMetaDTO, List<ColumnMetaDTO> columns) {
        // 1. Validation
        if (!tableMetaDTO.getTblNm().matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Table name must contain only letters, numbers, and underscores.");
        }
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("At least one column is required.");
        }

        boolean hasPk = false;
        for (ColumnMetaDTO col : columns) {
            if ("Y".equals(col.getIsPk())) {
                hasPk = true;
            }
            if ("Y".equals(col.getIsAutoIncrement())) {
                if (!"Y".equals(col.getIsPk())) {
                    throw new IllegalArgumentException("Auto Increment can only be set on Primary Key columns.");
                }
                // Check if type is numeric (assuming Type IDs 1=INT, 7=FLOAT, 8=DOUBLE from
                // schema.sql implies numerics)
                // simpler check: just ensure it's not a text type if we had type info here, but
                // for now we rely on UI/User.
            }
        }
        if (!hasPk) {
            throw new IllegalArgumentException("At least one Primary Key column is required.");
        }

        // 2. Save Table
        metaMapper.saveTable(tableMetaDTO);

        // 3. Save Columns
        int order = 1;
        for (ColumnMetaDTO col : columns) {
            col.setTblId(tableMetaDTO.getTblId());
            col.setOrderNo(order++);
            metaMapper.saveColumn(col);
        }
    }

    @Transactional
    public void createTable(TableMetaDTO tableMetaDTO) {
        // Deprecated or simple usage
        metaMapper.saveTable(tableMetaDTO);
    }

    @Transactional(readOnly = true)
    public List<TableMetaDTO> getTablesByUserId(Integer userId) {
        return metaMapper.findTablesByUserId(userId);
    }

    @Transactional(readOnly = true)
    public TableMetaDTO getTableById(Integer tblId) {
        return metaMapper.findTableById(tblId);
    }

    @Transactional
    public void addColumn(ColumnMetaDTO columnMetaDTO) {
        metaMapper.saveColumn(columnMetaDTO);
    }

    @Transactional(readOnly = true)
    public List<ColumnMetaDTO> getColumnsByTableId(Integer tblId) {
        return metaMapper.findColumnsByTableId(tblId);
    }

    @Transactional(readOnly = true)
    public List<DataTypeDTO> getAllDataTypes() {
        return metaMapper.findAllDataTypes();
    }

    @Transactional
    public void modifyColumn(ColumnMetaDTO columnMetaDTO) {
        // In a clearer implementation, we should also update TBL_SAMPLE data keys here
        // if colNm changes.
        // For this step, we focus on Meta update.
        metaMapper.updateColumn(columnMetaDTO);
    }

    @Transactional
    public void removeColumn(Integer colId) {
        metaMapper.deleteColumn(colId);
    }
}
