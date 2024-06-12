package com.rak.budget.service.budget.mapper;

import com.rak.budget.controller.budget.dto.BudgetCategoryDto;
import com.rak.budget.dao.model.budget.BudgetCategory;
import org.springframework.stereotype.Component;

@Component
public class BudgetCategoryDtoMapper {

    public BudgetCategoryDto map(BudgetCategory entity) {
        return BudgetCategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public BudgetCategory map(BudgetCategoryDto dto) {
        var result = new BudgetCategory();
        result.setId(dto.getId());
        result.setName(dto.getName());
        return result;
    }
}
