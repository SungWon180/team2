package com.team2.householdledger.ledger.mapper;

import com.team2.householdledger.ledger.dto.LedgerDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LedgerMapper {

    /**
     * 가계부 내역 조회 (전체 or 필터)
     * TODO: 검색 조건 필요 시 파라미터 추가 (SearchDTO 등)
     */
    List<LedgerDTO> selectLedgerList(Long userNo);

    /**
     * 가계부 내역 등록
     */
    void insertLedger(LedgerDTO ledger);

    /**
     * 가계부 내역 수정
     */
    void updateLedger(LedgerDTO ledger);

    /**
     * 가계부 내역 삭제 (Soft Delete: status_cd = 'N')
     */
    void deleteLedger(Long ledgerNo);
}
