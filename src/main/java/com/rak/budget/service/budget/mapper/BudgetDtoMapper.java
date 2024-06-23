package com.rak.budget.service.budget.mapper;

import com.rak.budget.controller.budget.dto.BudgetDto;
import com.rak.budget.controller.budget.dto.BudgetViewDto;
import com.rak.budget.dao.model.budget.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetDtoMapper {

    private final BudgetCategoryDtoMapper categoryDtoMapper;

    public BudgetDto map(Budget entity) {
        return BudgetDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .userId(entity.getUserId())
                .categoryId(entity.getCategory().getId())
                .build();
    }

    public BudgetViewDto mapView(Budget entity) {
        return BudgetViewDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .userId(entity.getUserId())
                .category(categoryDtoMapper.map(entity.getCategory()))
                .build();
    }

}
