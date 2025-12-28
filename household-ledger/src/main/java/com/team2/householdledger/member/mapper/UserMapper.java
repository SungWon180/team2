package com.team2.householdledger.member.mapper;

import com.team2.householdledger.member.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUserId(String userId);

    void save(User user);
}
