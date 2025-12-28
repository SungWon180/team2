package com.team2.householdledger.common.service;

import com.team2.householdledger.common.dto.CommCode;
import com.team2.householdledger.common.mapper.CommCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeService {

    private final CommCodeMapper commCodeMapper;

    /**
     * 그룹 코드로 공통 코드 목록 조회
     * 예: "INC" -> [월급, 용돈, ...]
     */
    public List<CommCode> getCodesByGroup(String grpCd) {
        return commCodeMapper.findByGrpCd(grpCd);
    }

    /**
     * 코드로 코드명 조회
     * 예: "INC01" -> "월급"
     */
    public String getCodeName(String commCd) {
        return commCodeMapper.findNmByCommCd(commCd);
    }
}
