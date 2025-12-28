package com.team2.householdledger.stats.dto;

import lombok.Data;

/**
 * 통계용 DTO
 */
@Data
public class StatsDTO {
    // 카테고리별 통계용
    private String commCd; // 카테고리 코드
    private String categoryNm; // 카테고리명 (fn_get_comm_nm)
    private Long totalAmount; // 총 금액 (SUM)

    // 월별 통계용
    private String month; // '2025-01' 등의 포맷
    private Long incomeSum; // 수입 합계
    private Long expenseSum; // 지출 합계
}
