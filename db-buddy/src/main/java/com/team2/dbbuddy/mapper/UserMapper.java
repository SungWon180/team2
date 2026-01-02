package com.team2.dbbuddy.mapper;

import com.team2.dbbuddy.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int save(UserDTO userDTO);

    UserDTO findByEmail(String email);

    int countByEmail(String email);
}
