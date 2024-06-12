package com.rak.budget.controller.budget.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetDto {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String categoryid;
}
