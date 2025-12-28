package com.team2.householdledger.ledger.controller;

import com.team2.householdledger.common.dto.CommonDTO;
import com.team2.householdledger.ledger.dto.LedgerDTO;
import com.team2.householdledger.ledger.service.LedgerService;
import com.team2.householdledger.member.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    /**
     * 가계부 목록 조회
     */
    @GetMapping("/list")
    public CommonDTO<List<LedgerDTO>> getList(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return CommonDTO.error(401, "로그인이 필요합니다.");
        }

        List<LedgerDTO> list = ledgerService.getLedgerList(loginUser.getUserNo());
        return CommonDTO.success(list);
    }

    /**
     * 가계부 등록
     */
    @PostMapping("/write")
    public CommonDTO<String> write(@RequestBody LedgerDTO ledger, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return CommonDTO.error(401, "로그인이 필요합니다.");
        }

        ledger.setUserNo(loginUser.getUserNo()); // 작성자 설정
        ledgerService.registerLedger(ledger);
        return CommonDTO.success("등록되었습니다.");
    }

    // TODO: 수정(PUT), 삭제(DELETE) 메서드 구현 필요
}
