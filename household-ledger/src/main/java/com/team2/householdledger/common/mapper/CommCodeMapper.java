package com.team2.householdledger.common.mapper;

import com.team2.householdledger.common.dto.CommCode;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommCodeMapper {
    // 특정 그룹 코드의 모든 공통 코드 조회
    List<CommCode> findByGrpCd(String grpCd);

    // 코드값으로 코드명 조회
    String findNmByCommCd(String commCd);
}
