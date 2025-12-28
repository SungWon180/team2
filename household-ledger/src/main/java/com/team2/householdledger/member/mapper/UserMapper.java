package com.team2.householdledger.member.mapper;

import com.team2.householdledger.member.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDTO findByUserId(String userId);

    void save(UserDTO user);
}
