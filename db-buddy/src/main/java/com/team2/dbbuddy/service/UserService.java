package com.team2.dbbuddy.service;

import com.team2.dbbuddy.dto.UserDTO;
import com.team2.dbbuddy.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Transactional
    public void register(UserDTO userDTO) {
        if (userMapper.countByEmail(userDTO.getEmail()) > 0) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다");
        }
        // In a real application, password encryption is essential.
        // For this educational project, we might skip it or implement a simple hash
        // suitable for the learning scope,
        // but as per requirements, we should store it.
        userMapper.save(userDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO login(String email, String password) {
        UserDTO user = userMapper.findByEmail(email);
        if (user != null && user.getPasswd().equals(password)) {
            return user;
        }
        return null;
    }
}
