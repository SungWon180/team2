package com.team2.householdledger.stats.controller;

import com.team2.householdledger.common.dto.CommonDTO;
import com.team2.householdledger.stats.dto.StatsDTO;
import com.team2.householdledger.stats.service.StatsService;
import com.team2.householdledger.member.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * 카테고리별 통계 조회
     * 예: /api/stats/category?yearMonth=2025-05
     */
    @GetMapping("/category")
    public CommonDTO<List<StatsDTO>> getCategoryStats(@RequestParam String yearMonth, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return CommonDTO.error(401, "로그인이 필요합니다.");
        }

        List<StatsDTO> list = statsService.getCategoryStats(loginUser.getUserNo(), yearMonth);
        return CommonDTO.success(list);
    }

    // TODO: 월별 통계 API 추가 필요
}
