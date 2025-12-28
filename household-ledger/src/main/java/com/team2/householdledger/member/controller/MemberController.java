package com.team2.householdledger.member.controller;

import com.team2.householdledger.common.dto.CommonDTO;
import com.team2.householdledger.member.dto.UserDTO;
import com.team2.householdledger.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;

    /**
     * 회원 가입
     * TODO: ID 중복 체크 로직 필요 시 추가 (/check-id)
     */
    @PostMapping("/join")
    public CommonDTO<String> join(@RequestBody UserDTO user) {
        // TODO: 비밀번호 암호화 필수
        userService.join(user);
        return CommonDTO.success("회원가입이 완료되었습니다.");
    }

    /**
     * 내 정보 조회
     */
    @GetMapping("/info")
    public CommonDTO<UserDTO> getMyInfo(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return CommonDTO.error(401, "로그인이 필요합니다.");
        }

        // 세션 정보보다 DB 최신 정보를 조회하는 것이 안전
        // UserDTO info = userService.getUserInfo(loginUser.getUserNo());
        return CommonDTO.success(loginUser);
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update")
    public CommonDTO<String> updateInfo(@RequestBody UserDTO user, HttpSession session) {
        // TODO: 서비스에 update 로직 구현 필요
        return CommonDTO.success("수정되었습니다.");
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     */
    @DeleteMapping("/withdraw")
    public CommonDTO<String> withdraw(HttpSession session) {
        // TODO: 서비스에 withdraw 로직 구현 필요 (status_cd = 'N')
        return CommonDTO.success("탈퇴 처리되었습니다.");
    }
}
