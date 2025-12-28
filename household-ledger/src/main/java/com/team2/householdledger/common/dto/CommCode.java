package com.team2.householdledger.common.dto;

import lombok.Data;

@Data
public class CommCode {
    private String commCd; // 코드
    private String grpCd; // 그룹코드
    private String commNm; // 코드명
    private Integer sortNo; // 정렬순서
    private String useYn; // 사용여부
}
