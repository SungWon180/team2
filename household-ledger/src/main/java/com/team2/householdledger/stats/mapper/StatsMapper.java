package com.team2.householdledger.stats.mapper;

import com.team2.householdledger.stats.dto.StatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StatsMapper {

    /**
     * 카테고리별 지출 통계
     * 
     * @param userNo    사용자 번호
     * @param yearMonth 조회할 연월 (예: '2025-05')
     */
    List<StatsDTO> selectCategoryStats(@Param("userNo") Long userNo, @Param("yearMonth") String yearMonth);

    /**
     * 월별 수입/지출 합계
     */
    List<StatsDTO> selectMonthlyStats(@Param("userNo") Long userNo, @Param("year") String year);
}
