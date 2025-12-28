package com.team2.householdledger.ledger.service;

import com.team2.householdledger.ledger.dto.LedgerDTO;
import com.team2.householdledger.ledger.mapper.LedgerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerMapper ledgerMapper;

    /**
     * 내역 목록 조회
     * TODO: 페이지네이션 로직 추가 고려
     */
    public List<LedgerDTO> getLedgerList(Long userNo) {
        return ledgerMapper.selectLedgerList(userNo);
    }

    /**
     * 내역 등록
     */
    @Transactional
    public void registerLedger(LedgerDTO ledger) {
        // TODO: 유효성 검사 (금액 0원 이상 등)
        ledgerMapper.insertLedger(ledger);
    }

    /**
     * 내역 수정
     * TODO: 본인 글인지 확인하는 로직 필요 (Controller 레벨 or 여기서 체크)
     */
    @Transactional
    public void modifyLedger(LedgerDTO ledger) {
        ledgerMapper.updateLedger(ledger);
    }

    /**
     * 내역 삭제
     */
    @Transactional
    public void removeLedger(Long ledgerNo) {
        ledgerMapper.deleteLedger(ledgerNo);
    }
}
