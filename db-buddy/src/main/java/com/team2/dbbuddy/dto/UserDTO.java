package com.team2.dbbuddy.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Integer userId;
    private String userNm;
    private String email;
    private String passwd;
    private String activeFl;
    private LocalDateTime regDt;
}
