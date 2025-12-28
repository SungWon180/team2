package com.team2.householdledger.member.service;

import com.team2.householdledger.member.dto.User;
import com.team2.householdledger.member.mapper.UserMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    /**
     * 로그인
     */
    public User login(String userId, String userPw) {
        User user = userMapper.findByUserId(userId);
        // 간단한 비밀번호 비교 (실무에서는 암호화 필수)
        if (user != null && user.getUserPw().equals(userPw)) {
            return user;
        }
        return null;
    }

    /**
     * 회원가입 (테스트용)
     */
    public void join(User user) {
        userMapper.save(user);
    }
}
