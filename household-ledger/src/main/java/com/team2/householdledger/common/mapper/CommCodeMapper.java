package com.team2.householdledger.common.mapper;

import com.team2.householdledger.common.dto.CommCodeDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommCodeMapper {
    // 특정 그룹 코드의 모든 공통 코드 조회
    List<CommCodeDTO> findByGrpCd(String grpCd);

}
