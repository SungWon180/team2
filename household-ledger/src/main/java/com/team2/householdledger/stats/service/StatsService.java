package com.team2.householdledger.stats.service;

import com.team2.householdledger.stats.dto.StatsDTO;
import com.team2.householdledger.stats.mapper.StatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsMapper statsMapper;

    /**
     * 이번 달 카테고리별 통계
     */
    public List<StatsDTO> getCategoryStats(Long userNo, String yearMonth) {
        return statsMapper.selectCategoryStats(userNo, yearMonth);
    }

    /**
     * 연간 월별 추이 통계
     */
    public List<StatsDTO> getMonthlyStats(Long userNo, String year) {
        return statsMapper.selectMonthlyStats(userNo, year);
    }
}
