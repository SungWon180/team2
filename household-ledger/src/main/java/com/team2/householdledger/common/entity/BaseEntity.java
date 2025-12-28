package com.team2.householdledger.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
