package com.team2.householdledger.member.controller;

import com.team2.householdledger.member.service.UserService;
import com.team2.householdledger.member.dto.User;
import com.team2.householdledger.common.dto.CommonDTO;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public CommonDTO<User> login(@RequestBody User loginRequest, HttpSession session) {
        User user = userService.login(loginRequest.getUserId(), loginRequest.getUserPw());
        if (user != null) {
            session.setAttribute("loginUser", user);
            return CommonDTO.success(user);
        } else {
            return CommonDTO.error(401, "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @PostMapping("/logout")
    public CommonDTO<String> logout(HttpSession session) {
        session.invalidate();
        return CommonDTO.success("로그아웃 되었습니다.");
    }
}
