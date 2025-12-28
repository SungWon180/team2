package com.team2.householdledger.member.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long userNo; // user_no
    private String userId; // user_id
    private String userPw; // user_pw
    private String userNm; // user_nm
    private String email; // email
    private String statusCd; // status_cd
    private LocalDateTime regDt; // reg_dt
    private LocalDateTime modDt; // mod_dt
}
