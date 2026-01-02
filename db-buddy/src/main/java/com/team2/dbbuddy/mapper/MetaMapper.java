package com.team2.dbbuddy.mapper;

import com.team2.dbbuddy.dto.ColumnMetaDTO;
import com.team2.dbbuddy.dto.DataTypeDTO;
import com.team2.dbbuddy.dto.TableMetaDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MetaMapper {
    // Table Meta
    int saveTable(TableMetaDTO tableMetaDTO);

    List<TableMetaDTO> findTablesByUserId(Integer userId);

    TableMetaDTO findTableById(Integer tblId);

    // Column Meta
    int saveColumn(ColumnMetaDTO columnMetaDTO);

    List<ColumnMetaDTO> findColumnsByTableId(Integer tblId);

    int updateColumn(ColumnMetaDTO columnMetaDTO);

    int deleteColumn(Integer colId);

    // Data Types
    List<DataTypeDTO> findAllDataTypes();
}
