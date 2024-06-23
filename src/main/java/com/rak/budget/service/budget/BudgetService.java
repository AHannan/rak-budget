package com.rak.budget.service.budget;

import com.rak.budget.controller.budget.dto.BudgetDto;
import com.rak.budget.controller.budget.dto.BudgetViewDto;
import com.rak.budget.dao.model.budget.Budget;
import com.rak.budget.dao.model.budget.BudgetCategory;
import com.rak.budget.dao.repository.budget.BudgetRepository;
import com.rak.budget.service.budget.mapper.BudgetDtoMapper;
import com.rak.budget.service.budget.specification.BudgetSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repository;
    private final BudgetCategoryService budgetCategoryService;
    private final BudgetDtoMapper mapper;

    public Page<BudgetViewDto> getAll(String userId, String categoryId, Pageable pageable) {
        return repository.findAll(BudgetSpecification.buildSpecification(userId, categoryId), pageable)
                .map(mapper::mapView);
    }

    public Optional<BudgetViewDto> getById(String id) {
        return repository.findById(id)
                .map(mapper::mapView);
    }

    public Optional<BudgetViewDto> getBudgetByCategoryIdAndUserId(String categoryId, String userId) {
        return repository.findByCategoryIdAndUserId(categoryId, userId)
                .map(mapper::mapView);
    }

    public BudgetDto create(BudgetDto dto) {
        if (repository.existsByUserIdAndCategoryId(dto.getUserId(), dto.getCategoryId())) {
            throw new IllegalStateException("User already has a budget for category id " + dto.getCategoryId());
        }

        var result = new Budget();
        result.setUserId(dto.getUserId());
        result.setAmount(dto.getAmount());
        result.setCategory(toCategory(dto));

        return mapper.map(repository.save(result));
    }

    public Optional<BudgetDto> update(String id, BudgetDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setAmount(dto.getAmount());
            existing.setCategory(toCategory(dto));
            return mapper.map(repository.save(existing));
        });
    }

    private BudgetCategory toCategory(BudgetDto dto) {
        return budgetCategoryService.findById(dto.getCategoryId());
    }

    public boolean delete(String id) {
        var result = repository.findById(id);
        if (result.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
